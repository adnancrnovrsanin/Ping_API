package com.adnancrnovrsanin.ping.userChat;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserChatRepository extends CrudRepository<UserChat, Long> {
    Optional<UserChat> findByUser_PhoneNumberAndChat_Id(String phoneNumber, String id);

    Iterable<UserChat> findAllByUser_PhoneNumber(String phoneNumber);
}
