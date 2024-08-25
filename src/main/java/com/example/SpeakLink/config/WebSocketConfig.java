package com.example.SpeakLink.config;


import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.RoomMembers;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoomMembersRepository;
import com.example.SpeakLink.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Optional;
import java.util.UUID;
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{

	private final RoomMembersRepository roomMembersRepository;
	private final UserRepository userRepository;

	public WebSocketConfig(RoomMembersRepository roomMembersRepository, UserRepository userRepository) {
		this.roomMembersRepository = roomMembersRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker( "/room");
		config.setApplicationDestinationPrefixes("/app");
		config.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
				.addEndpoint("/ws")
				//.setAllowedOrigins("*")
				.withSockJS();
	}

	/*@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
		resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(new ObjectMapper());
		converter.setContentTypeResolver(resolver);
		messageConverters.add(converter);
		return false;
	}*/

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
				if(StompCommand.SUBSCRIBE.equals(accessor.getCommand()) && accessor.getDestination() != null)
				{
					if(accessor.getDestination().startsWith("/room/"))
					{
						String stringRoomId = accessor.getDestination().replace("/room/", "");
						UUID roomId = UUID.fromString(stringRoomId);
						User user = userRepository.findByEmail(accessor.getUser().getName());
						Room room = new Room();
						room.setId(roomId);
						Optional<RoomMembers> access = roomMembersRepository.findById(new RoomMembers.RoomMembersPk(user, room));
						if (access.isPresent() && access.get().isInviteStatus()) {
							return ChannelInterceptor.super.preSend(message, channel);
						}
					}
					throw new AccessDeniedException();
				}
				return ChannelInterceptor.super.preSend(message, channel);
			}
		});
		//WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
	}
}
