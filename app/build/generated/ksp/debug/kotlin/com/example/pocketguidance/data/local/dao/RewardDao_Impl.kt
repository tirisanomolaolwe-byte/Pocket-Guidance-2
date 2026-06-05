package com.example.pocketguidance.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.pocketguidance.`data`.model.Reward
import javax.`annotation`.processing.Generated
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
public class RewardDao_Impl(
  __db: RoomDatabase,
) : RewardDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfReward: EntityInsertAdapter<Reward>
  init {
    this.__db = __db
    this.__insertAdapterOfReward = object : EntityInsertAdapter<Reward>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `rewards` (`id`,`userId`,`title`,`date`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Reward) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.userId.toLong())
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.date)
      }
    }
  }

  public override suspend fun insert(reward: Reward): Unit = performSuspending(__db, false, true) {
      _connection ->
    __insertAdapterOfReward.insert(_connection, reward)
  }

  public override suspend fun getRewards(userId: Int): List<Reward> {
    val _sql: String = "SELECT * FROM rewards WHERE userId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _result: MutableList<Reward> = mutableListOf()
        while (_stmt.step()) {
          val _item: Reward
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          _item = Reward(_tmpId,_tmpUserId,_tmpTitle,_tmpDate)
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
