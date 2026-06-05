package com.example.pocketguidance

import com.example.pocketguidance.data.local.dao.UserDao
import com.example.pocketguidance.data.model.User
import com.example.pocketguidance.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var repository: UserRepository
    private lateinit var fakeUserDao: FakeUserDao

    @Before
    fun setUp() {
        fakeUserDao = FakeUserDao()
        repository = UserRepository(fakeUserDao)
    }

    @Test
    fun register_new_user_returns_true() = runBlocking {
        val result = repository.register("test", "test@mail.com", "pass")
        assertTrue(result)
        assertEquals(1, fakeUserDao.users.size)
    }

    @Test
    fun register_existing_user_returns_false() = runBlocking {
        repository.register("test", "test@mail.com", "pass")
        val result = repository.register("test", "other@mail.com", "other")
        assertFalse(result)
        assertEquals(1, fakeUserDao.users.size)
    }

    @Test
    fun login_with_correct_credentials_returns_user() = runBlocking {
        repository.register("test", "test@mail.com", "pass")
        val user = repository.login("test", "pass")
        assertNotNull(user)
        assertEquals("test", user?.name)
    }

    @Test
    fun login_with_incorrect_credentials_returns_null() = runBlocking {
        repository.register("test", "test@mail.com", "pass")
        val user = repository.login("test", "wrong")
        assertNull(user)
    }
}

class FakeUserDao : UserDao {
    val users = mutableListOf<User>()
    
    override suspend fun insert(user: User) {
        users.add(user)
    }

    override suspend fun login(username: String, password: String): User? {
        return users.find { it.name == username && it.password == password }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return users.find { it.name == username }
    }
}
