package com.adnancrnovrsanin.ping.userChat;

import com.adnancrnovrsanin.ping.chat.Chat;
import com.adnancrnovrsanin.ping.user.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_chat")
public class UserChat {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isAdmin;
    private boolean isCreator;
    private boolean didUpdateLast;

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        UserChat userChat = (UserChat) object;

        return chat.equals(userChat.chat) && user.equals(userChat.user) || id.equals(userChat.id);
    }

    public int hashCode() {
        return id.hashCode() + chat.hashCode() + user.hashCode();
    }
}
