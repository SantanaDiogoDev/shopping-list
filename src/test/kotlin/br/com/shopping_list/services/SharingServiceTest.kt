import br.com.shopping_list.entities.Sharing
import br.com.shopping_list.repositories.SharingRepository
import br.com.shopping_list.services.SharingService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class SharingServiceTest {

    private val sharingRepository = mock(SharingRepository::class.java)

    private val sharingService = SharingService(sharingRepository)

    @Test
    fun `test find sharing by id successfully`() {
        val sharingId = UUID.randomUUID()
        val sharing = Sharing(
            id = sharingId,
            listId = UUID.randomUUID(),
            userId = UUID.randomUUID()
        )

        `when`(sharingRepository.findById(sharingId)).thenReturn(Optional.of(sharing))

        val result = sharingService.findById(sharingId)

        assertEquals(sharingId, result.id)
        assertEquals(sharing.listId, result.listId)
        assertEquals(sharing.userId, result.userId)
    }

    @Test
    fun `test find sharing by id throws exception when not found`() {
        val sharingId = UUID.randomUUID()

        `when`(sharingRepository.findById(sharingId)).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            sharingService.findById(sharingId)
        }
    }

    @Test
    fun `test find all sharings`() {
        val sharing1 = Sharing(
            id = UUID.randomUUID(),
            listId = UUID.randomUUID(),
            userId = UUID.randomUUID()
        )
        val sharing2 = Sharing(
            id = UUID.randomUUID(),
            listId = UUID.randomUUID(),
            userId = UUID.randomUUID()
        )

        `when`(sharingRepository.findAll()).thenReturn(listOf(sharing1, sharing2))

        val result = sharingService.findAll()

        assertEquals(2, result.size)
        assertEquals(sharing1.id, result[0].id)
        assertEquals(sharing2.id, result[1].id)
    }
}
