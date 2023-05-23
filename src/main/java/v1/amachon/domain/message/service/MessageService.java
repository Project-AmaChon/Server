package v1.amachon.domain.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseEntity;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.message.dto.MessageDto;
import v1.amachon.domain.message.dto.SendMessageDto;
import v1.amachon.domain.message.dto.MessageRoomDto;
import v1.amachon.domain.message.entity.Message;
import v1.amachon.domain.message.entity.MessageRoom;
import v1.amachon.domain.message.repository.MessageRepository;
import v1.amachon.domain.message.repository.MessageRoomRepository;
import v1.amachon.global.config.security.util.SecurityUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static v1.amachon.domain.base.BaseResponseStatus.*;

@RequiredArgsConstructor
@Transactional
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final MemberRepository memberRepository;

    public void send(MessageRoom messageRoom, SendMessageDto sendMessageDto) {
        Message from = Message.builder().messageRoom(messageRoom)
                .sender(messageRoom.getFrom())
                .receiver(messageRoom.getTo())
                .content(sendMessageDto.getContent())
                .build();
        Message to = Message.builder().messageRoom(messageRoom.getToSend())
                .sender(messageRoom.getFrom())
                .receiver(messageRoom.getTo())
                .content(sendMessageDto.getContent())
                .build();
        messageRepository.save(from);
        messageRepository.save(to);
        messageRoom.sendMessage(from, to);
        messageRoomRepository.save(messageRoom);
        messageRoomRepository.save(messageRoom.getToSend());
    }

    public void sendMessageByMemberId(Long memberId, SendMessageDto sendMessageDto) throws BaseException {
        Member from = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        Member to = memberRepository.findById(memberId).orElseThrow(
                () -> new BaseException(NOT_FOUND_USERS_ID));
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
        send(messageRoom.get(), sendMessageDto);
    }

    public void sendMessageByRoomId(Long roomId, SendMessageDto sendMessageDto) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        MessageRoom messageRoom = messageRoomRepository.findByIdFetch(roomId).orElseThrow(
                () -> new BaseException(NOT_FOUND_MESSAGE_ROOM));
        if (member.getId() != messageRoom.getFrom().getId()) {
            throw new BaseException(BAD_REQUEST);
        }
        send(messageRoom, sendMessageDto);
    }

    public List<MessageRoomDto> getMessageRooms() throws BaseException {
        Member member = memberRepository.findByEmailFetchMessageRoom(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        return member.getMessageRooms().stream().map(MessageRoomDto::new).collect(Collectors.toList());
    }

    public List<MessageDto> getMessages(Long roomId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        MessageRoom messageRoom = messageRoomRepository.findByIdFetchMessages(roomId).orElseThrow(
                () -> new BaseException(NOT_FOUND_MESSAGE_ROOM));
        messageRoom.initUnReadMessageCount();
        messageRoomRepository.save(messageRoom);
        if (member.getId() != messageRoom.getFrom().getId()) {
            throw new BaseException(BAD_REQUEST);
        }
        return messageRoom.getMessages().stream().map(MessageDto::new).collect(Collectors.toList());
    }

    public MessageDto getMessageDetail(Long messageId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        Message message = messageRepository.findByIdFetch(messageId).orElseThrow(
                () -> new BaseException(NOT_FOUND_MESSAGE));
        if (member.getId() != message.getSender().getId()) {
            throw new BaseException(BAD_REQUEST);
        }
        message.getMessageRoom().initUnReadMessageCount();
        return new MessageDto(message);
    }

    public void deleteMessage(Long messageId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        Message message = messageRepository.findByIdFetch(messageId).orElseThrow(
                () -> new BaseException(NOT_FOUND_MESSAGE));
        if (member.getId() != message.getSender().getId()) {
            throw new BaseException(INVALID_USER);
        }
        message.expired();
        messageRepository.save(message);
    }

    public void leaveMessageRoom(Long roomId) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(UNAUTHORIZED));
        MessageRoom messageRoom = messageRoomRepository.findByIdFetchMessages(roomId).orElseThrow(
                () -> new BaseException(NOT_FOUND_MESSAGE_ROOM));
        for (Message message : messageRoom.getMessages()) {
            message.expired();
            messageRepository.save(message);
        }
        messageRoom.expired();
    }
}
