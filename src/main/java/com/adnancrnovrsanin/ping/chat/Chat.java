package com.adnancrnovrsanin.ping.chat;

import com.adnancrnovrsanin.ping.message.Message;
import com.adnancrnovrsanin.ping.message.MessageType;
import com.adnancrnovrsanin.ping.userChat.UserChat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;
    private String chatName;
    private String chatImageUrl;
    private String chatDescription;

    @OneToMany(
            mappedBy = "chat",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Message> messages;
    @Enumerated(EnumType.STRING)
    private ChatType chatType;
    private Date createdAt;
    private Date updatedAt;
    @OneToMany(
            mappedBy = "chat",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserChat> users;

    public void addUser(UserChat user) {
        if (users == null) {
            users = new HashSet<>(Set.of(user));
        } else {
            users.add(user);
        }
        this.updatedAt = new Date();
    }

    public void removeUser(UserChat user) {
        users.remove(user);
        this.updatedAt = new Date();
    }

    public void addMessage(Message message) {
        if (messages == null) {
            messages = new HashSet<>(Set.of(message));
        } else {
            messages.add(message);
        }
        this.updatedAt = new Date();
    }

    public void removeMessage(Message message) {
        messages.remove(message);
        this.updatedAt = new Date();
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
        this.updatedAt = new Date();
    }

    public void setChatImageUrl(String chatImageUrl) {
        this.chatImageUrl = chatImageUrl;
        this.updatedAt = new Date();
    }

    public void setChatDescription(String chatDescription) {
        this.chatDescription = chatDescription;
        this.updatedAt = new Date();
    }

    public String getLatestMessage() {
        if (messages == null) {
            return null;
        }
        var variable = messages.stream().max((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt())).orElse(null);
        if (variable == null) {
            return null;
        }

        if (variable.getMessageType() == MessageType.IMAGE)
            return "Photo";
        if (variable.getMessageType() == MessageType.VIDEO)
            return "Video";
        if (variable.getMessageType() == MessageType.AUDIO)
            return "Audio";
        if (variable.getMessageType() == MessageType.DOCUMENT)
            return "File";

        return variable.getMessage();
    }

    public int hashCode() {
        return id.hashCode();
    }
}
