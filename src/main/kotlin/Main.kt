import ChatService.addChat
import ChatService.createMessage
import ChatService.editMessage
import ChatService.getChats
import ChatService.getCountById
import ChatService.getLastMessage
//import ChatService.getLastMessages
import ChatService.getMessagesById
import ChatService.getNotReadChats
import ChatService.getUnreadChatsCount
//import ChatService.getNotReadMessages
import ChatService.readMessages

data class Chats(
    var chatId: Int = 0,
    var title: String? = null,
    var messages: MutableMap<Int/*chatId*/, MessageService> = mutableMapOf()
)

data class MessageService(
    val messageId: Int = 0,
    val chatId: Int = 0, //id пользователя
    var message: String? = null, //Текст сообщения
    var notReadMessage: Boolean = true, //Прочитано ли сообщение
    var deletedMessage: Boolean = false
)

object ChatService {
    var mapChats = mutableMapOf<Int/*chatId*/, Chats>()
    var listReads = mutableListOf<Chats?>()
    var chatId: Int = 0 // Счётчик для создания id чатов
    var messageId: Int = 0 // Счётчик для создания id сообщений
    var notReadMessage: Boolean = true

    fun clear() { // Сбрасываем поля в исходные значения
        mapChats = mutableMapOf()
        chatId = 0
        messageId = 0
    }
    fun addChat(title: String?, message: String): Int {
        mapChats[++chatId] = Chats(chatId = chatId, title = title)
        createMessage(chatId = chatId, message = message)
        listReads.remove(mapChats[chatId])
        return chatId
    }

    fun createMessage(chatId: Int, message: String): Int {
        val newMessage = mapChats[chatId] ?: return -1
        newMessage.messages[++messageId] = MessageService(messageId, chatId, message)
        listReads.add(mapChats[chatId])
        return messageId
    }

    fun deleteChat(chatId: Int): Boolean {
        mapChats.remove(chatId) ?: return false
        return true
    }

    fun deleteMessage(chatId: Int, messageId: Int): Boolean {
        val chat = mapChats[chatId] ?: return false
        val newMessage = chat.messages[messageId] ?: return false
        newMessage.deletedMessage = true
        return true
    }

    fun editMessage(chatId: Int, messageId: Int, message: String): Boolean {
        val chat = mapChats[chatId] ?: return false
        val newMessage = chat.messages[messageId] ?: return false
        newMessage.message = message
        return true
    }

    fun restoreMessage(chatId: Int, messageId: Int): Boolean {
        val chat = mapChats[chatId] ?: return false
        val newMessage = chat.messages[messageId] ?: return false
        newMessage.deletedMessage = false
        return true
    }

    fun readMessages(chatId: Int, messageId: Int): Boolean {
        val chat = mapChats[chatId] ?: return false
        chat.messages[messageId]?.notReadMessage = false
        listReads.remove(mapChats[chatId])
        listReads.remove(mapChats[chatId])

        return true
    }

    fun getChats(): MutableMap<Int, Chats> {
        return mapChats
    }

    fun getById(chatId: Int): Chats? {
        return mapChats.getValue(chatId)
    }

    fun getMessagesById(chatId: Int): MutableMap<Int, MessageService>? {
        val chat = mapChats[chatId] ?: return null
        return chat.messages
    }

    fun getCountById(chatId: Int, messageNumbers: Int): MutableList<MessageService>? {
        val chat = mapChats[chatId] ?: return null
        val messagesList = chat.messages.values
            .toList()
            .asReversed()
            .asSequence()
            .take(messageNumbers)
            .onEach { it.notReadMessage = false }
            .toMutableList()
            return messagesList
    }

    fun getNotReadChats(): List<Chats> =
        mapChats.values.asSequence()
            .map { chat: Chats ->
            chat.copy(messages = chat.messages.filter { it.value.notReadMessage }
                .toMutableMap()) }
            .filter { it.messages.isNotEmpty() }
            .toList()

    fun getUnreadChatsCount(): Int = getNotReadChats().size

fun getLastMessage(): List<String?> = mapChats.values.asSequence()
    .toMutableList()
    .map { it.messages.values.lastOrNull()?.message ?: "Нет сообщений" }

}

fun main() {

    addChat("Alex Sergeev","hey bro")
    createMessage(1, "wow")
    readMessages(1, 1)
    readMessages(1, 2)
//    addNote("yui", "yui")
//    createComment(2, "nope")
//    editNote(1, "bye", "bye")
//    deleteComment(1, 1)
//    println(message)
//    println(getNotes())
//    restoreComment(1, 1)
//    println(getCommentsById(1))
//    deleteNote(1)
//    println(getNotes())
//    editMessage(1, 1, "bye")
//    println(getNotReadMessages(1))
    addChat("Alex","hey")
    createMessage(2, "aaa")
    println(getMessagesById(1))
    println(getChats())
    println(getNotReadChats())
    println(getUnreadChatsCount())
    println(getCountById(1, 2))
    println(getLastMessage())
}