package com.adnancrnovrsanin.ping.user;

import com.adnancrnovrsanin.ping.message.Message;
import com.adnancrnovrsanin.ping.user.User;
import com.adnancrnovrsanin.ping.userChat.UserChat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name="_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_phone_number_unique", columnNames = "phoneNumber")
        }
)
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    private String phoneNumber;
    private String displayName;
    private String about;
    private String profilePictureUrl;
    @OneToMany(mappedBy = "sender")
    private Set<Message> messages;

    @OneToMany(mappedBy = "user")
    private Set<UserChat> userChats;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    public void addUserChat(UserChat userChat) {
        if (userChats == null) {
            userChats = new HashSet<>(Set.of(userChat));
        } else {
            userChats.add(userChat);
        }
    }
    public void addMessage(Message message) {
        if (messages == null) {
            messages = new HashSet<>(Set.of(message));
        } else {
            messages.add(message);
        }
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    public boolean equals(Object object) {
//        if (object == this) {
//            return true;
//        }
//
//        if (object == null || getClass() != object.getClass()) {
//            return false;
//        }
//
//        User user = (User) object;
//        return phoneNumber.equals(user.getPhoneNumber());
//    }

    public int hashCode() {
        int result = 0;
        for (UserChat userChat: userChats) {
            result += userChat.getId().hashCode();
        }

        for (Message message: messages) {
            result += message.getId().hashCode();
        }

        return id.hashCode() + phoneNumber.hashCode() + result;
    }
}
