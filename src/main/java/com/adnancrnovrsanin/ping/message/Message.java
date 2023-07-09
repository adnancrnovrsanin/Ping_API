package com.adnancrnovrsanin.ping.message;

import com.adnancrnovrsanin.ping.chat.Chat;
import com.adnancrnovrsanin.ping.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;
    private String message;
    private String mediaUrl;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message replyTo;

    @OneToMany(mappedBy = "replyTo")
    private Set<Message> replies;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    public void addReply(Message message) {
        if (replies == null) {
            replies = new HashSet<>(Set.of(message));
        } else {
            replies.add(message);
        }
    }

    public int hashCode() {
        int result = 0;

        if (replies != null) {
            for (Message reply : replies) {
                result += reply.getId().hashCode();
            }
        }

        if (replyTo != null) {
            result += replyTo.getId().hashCode();
        }
        return id.hashCode() + chat.getId().hashCode() + result;
    }
}
