package v1.amachon.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.common.exception.BadRequestException;
import v1.amachon.common.exception.UnauthorizedException;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.member.service.exception.NotFoundMemberException;
import v1.amachon.message.service.dto.MessageDto;
import v1.amachon.message.service.dto.SendMessageRequest;
import v1.amachon.message.service.dto.MessageRoomDto;
import v1.amachon.message.entity.Message;
import v1.amachon.message.entity.MessageRoom;
import v1.amachon.message.repository.MessageRepository;
import v1.amachon.message.repository.MessageRoomRepository;
import v1.amachon.message.service.exception.NotFoundMessageException;
import v1.amachon.message.service.exception.NotFoundMessageRoomException;
import v1.amachon.common.config.security.util.SecurityUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final MemberRepository memberRepository;

    public void sendMessageByMemberId(Long memberId, SendMessageRequest sendMessageRequest) {
        Member from = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Member to = memberRepository.findById(memberId).orElseThrow(
                NotFoundMemberException::new);
        Optional<MessageRoom> messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId());
        if (messageRoom.isEmpty()) {
            MessageRoom fromMessageRoom = MessageRoom.builder().from(from).to(to).build();
            MessageRoom toMessageRoom = MessageRoom.builder().from(to).to(from).build();
            fromMessageRoom.match(toMessageRoom);
            messageRoomRepository.save(fromMessageRoom);
            messageRoomRepository.save(toMessageRoom);
            messageRoom = Optional.of(fromMessageRoom);
        } else if (messageRoom.get().getStatus() == BaseEntity.Status.EXPIRED) {
            messageRoom.get().init();
        }
        send(messageRoom.get(), sendMessageRequest);
    }

    public void sendMessageByRoomId(Long roomId, SendMessageRequest sendMessageRequest) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        MessageRoom messageRoom = messageRoomRepository.findByIdFetch(roomId)
                .orElseThrow(NotFoundMessageRoomException::new);
        if (member.getId() != messageRoom.getFrom().getId()) {
            throw new BadRequestException();
        }
        send(messageRoom, sendMessageRequest);
    }

    public void send(MessageRoom messageRoom, SendMessageRequest sendMessageRequest) {
        Message from = Message.builder().messageRoom(messageRoom)
                .sender(messageRoom.getFrom())
                .receiver(messageRoom.getTo())
                .content(sendMessageRequest.getContent())
                .build();
        Message to = Message.builder().messageRoom(messageRoom.getToSend())
                .sender(messageRoom.getFrom())
                .receiver(messageRoom.getTo())
                .content(sendMessageRequest.getContent())
                .build();
        messageRepository.save(from);
        messageRepository.save(to);
        messageRoom.sendMessage(from, to);
        messageRoomRepository.save(messageRoom);
        messageRoomRepository.save(messageRoom.getToSend());
    }

    public List<MessageRoomDto> getMessageRooms() {
        List<MessageRoom> messageRooms = messageRoomRepository.findByFromEmail(SecurityUtils.getLoggedUserEmail());
        return messageRooms.stream().map(MessageRoomDto::new).collect(Collectors.toList());
    }

    public List<MessageDto> getMessages(Long roomId) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        MessageRoom messageRoom = messageRoomRepository.findByIdFetchMessages(roomId)
                .orElseThrow(NotFoundMessageRoomException::new);
        messageRoom.initUnReadMessageCount();
        messageRoomRepository.save(messageRoom);
        if (member.getId() != messageRoom.getFrom().getId()) {
            throw new BadRequestException();
        }
        return messageRoom.getMessages().stream().map(MessageDto::new).collect(Collectors.toList());
    }

    public MessageDto getMessageDetail(Long messageId) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Message message = messageRepository.findByIdFetch(messageId)
                .orElseThrow(NotFoundMessageException::new);
        if (member.getId() != message.getSender().getId()) {
            throw new BadRequestException();
        }
        message.getMessageRoom().initUnReadMessageCount();
        return new MessageDto(message);
    }

    public void deleteMessage(Long messageId) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Message message = messageRepository.findByIdFetch(messageId)
                .orElseThrow(NotFoundMessageException::new);
        if (member.getId() != message.getSender().getId()) {
            throw new BadRequestException();
        }
        message.expired();
        messageRepository.save(message);
    }

    public void leaveMessageRoom(Long roomId) {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        MessageRoom messageRoom = messageRoomRepository.findByIdFetchMessages(roomId)
                .orElseThrow(NotFoundMessageException::new);
        if (member.getId() != messageRoom.getFrom().getId()) {
            throw new BadRequestException();
        }
        for (Message message : messageRoom.getMessages()) {
            message.expired();
        }
        messageRoom.expired();
    }
}
