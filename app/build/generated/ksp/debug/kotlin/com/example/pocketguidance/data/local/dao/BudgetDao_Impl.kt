package com.example.pocketguidance.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.pocketguidance.`data`.model.Budget
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class BudgetDao_Impl(
  __db: RoomDatabase,
) : BudgetDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfBudget: EntityInsertAdapter<Budget>
  init {
    this.__db = __db
    this.__insertAdapterOfBudget = object : EntityInsertAdapter<Budget>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `budgets` (`id`,`userId`,`minGoal`,`maxGoal`,`month`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Budget) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.userId.toLong())
        statement.bindDouble(3, entity.minGoal)
        statement.bindDouble(4, entity.maxGoal)
        statement.bindText(5, entity.month)
      }
    }
  }

  public override suspend fun insert(budget: Budget): Unit = performSuspending(__db, false, true) {
      _connection ->
    __insertAdapterOfBudget.insert(_connection, budget)
  }

  public override suspend fun getBudget(userId: Int, month: String): Budget? {
    val _sql: String = "SELECT * FROM budgets WHERE userId = ? AND month = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId.toLong())
        _argIndex = 2
        _stmt.bindText(_argIndex, month)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfMinGoal: Int = getColumnIndexOrThrow(_stmt, "minGoal")
        val _columnIndexOfMaxGoal: Int = getColumnIndexOrThrow(_stmt, "maxGoal")
        val _columnIndexOfMonth: Int = getColumnIndexOrThrow(_stmt, "month")
        val _result: Budget?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpMinGoal: Double
          _tmpMinGoal = _stmt.getDouble(_columnIndexOfMinGoal)
          val _tmpMaxGoal: Double
          _tmpMaxGoal = _stmt.getDouble(_columnIndexOfMaxGoal)
          val _tmpMonth: String
          _tmpMonth = _stmt.getText(_columnIndexOfMonth)
          _result = Budget(_tmpId,_tmpUserId,_tmpMinGoal,_tmpMaxGoal,_tmpMonth)
        } else {
          _result = null
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
