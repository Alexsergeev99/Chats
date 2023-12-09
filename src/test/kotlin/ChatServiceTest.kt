import ChatService.addChat
import ChatService.createMessage
import ChatService.deleteChat
import ChatService.deleteMessage
import ChatService.messageId
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }
    @Test
    fun addChatTest() {
        val result = addChat("hello", "world")
        assertEquals(1, result)
    }

    @Test
    fun deleteChatTest() {
        addChat("hello", "world")
        createMessage(1, "wow")
        deleteChat(1)
        val result = true
        assertEquals(true, result)
    }
    @Test
    fun createMessageTest() {
        addChat("hello", "world")
        createMessage(1, "wow")
        val result = messageId
        assertEquals(2, result)
    }
    @Test
    fun deleteMessageTest(){
        addChat("hello", "world")
        createMessage(1, "wow")
        deleteMessage(1, 1)
        val result = true
        assertEquals(true, result)
    }
}
