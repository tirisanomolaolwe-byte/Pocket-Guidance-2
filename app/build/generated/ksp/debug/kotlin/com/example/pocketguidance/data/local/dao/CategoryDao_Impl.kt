package com.example.pocketguidance.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.pocketguidance.`data`.model.Category
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CategoryDao_Impl(
  __db: RoomDatabase,
) : CategoryDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCategory: EntityInsertAdapter<Category>

  private val __deleteAdapterOfCategory: EntityDeleteOrUpdateAdapter<Category>
  init {
    this.__db = __db
    this.__insertAdapterOfCategory = object : EntityInsertAdapter<Category>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `categories` (`id`,`userId`,`name`,`maxLimit`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Category) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.userId.toLong())
        statement.bindText(3, entity.name)
        statement.bindDouble(4, entity.maxLimit)
      }
    }
    this.__deleteAdapterOfCategory = object : EntityDeleteOrUpdateAdapter<Category>() {
      protected override fun createQuery(): String = "DELETE FROM `categories` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Category) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
  }

  public override suspend fun insert(category: Category): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfCategory.insert(_connection, category)
  }

  public override suspend fun delete(category: Category): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfCategory.handle(_connection, category)
  }

  public override suspend fun getCategories(userId: Int): List<Category> {
    val _sql: String = "SELECT * FROM categories WHERE userId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfMaxLimit: Int = getColumnIndexOrThrow(_stmt, "maxLimit")
        val _result: MutableList<Category> = mutableListOf()
        while (_stmt.step()) {
          val _item: Category
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpMaxLimit: Double
          _tmpMaxLimit = _stmt.getDouble(_columnIndexOfMaxLimit)
          _item = Category(_tmpId,_tmpUserId,_tmpName,_tmpMaxLimit)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
