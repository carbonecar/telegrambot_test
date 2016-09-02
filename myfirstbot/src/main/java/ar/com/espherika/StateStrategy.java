package ar.com.espherika;

import ar.com.espherika.chatstrategies.BotChatStrategy;

public class StateStrategy {

	private ChatStates chatState;
	private BotChatStrategy botChatStrategy;

	public ChatStates getChatState() {
		return chatState;
	}

	public StateStrategy(ChatStates chatState, BotChatStrategy botChatStrategy) {
		super();
		this.chatState = chatState;
		this.botChatStrategy = botChatStrategy;
	}

	public void setChatState(ChatStates chatState) {
		this.chatState = chatState;
	}

	public BotChatStrategy getBotChatStrategy() {
		return botChatStrategy;
	}

	public void setBotChatStrategy(BotChatStrategy botChatStrategy) {
		this.botChatStrategy = botChatStrategy;
	}

}
