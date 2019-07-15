package info.nightscout.androidaps.database.daos

import androidx.room.Dao
import androidx.room.Query
import info.nightscout.androidaps.database.TABLE_TEMPORARY_BASALS
import info.nightscout.androidaps.database.embedments.InterfaceIDs
import info.nightscout.androidaps.database.entities.TemporaryBasal

@Suppress("FunctionName")
@Dao
abstract class TemporaryBasalDao : BaseDao<TemporaryBasal>() {

    @Query("SELECT * FROM $TABLE_TEMPORARY_BASALS WHERE id = :id")
    abstract override fun findById(id: Long): TemporaryBasal?

    @Query("SELECT * FROM $TABLE_TEMPORARY_BASALS WHERE pumpType = :pumpType AND pumpSerial = :pumpSerial AND startId < :endId AND pumpId IS NULL AND endId IS NULL AND ABS(timestamp - :timestamp) <= 86400000 ORDER BY startId DESC LIMIT 1")
    abstract fun getWithSmallerStartId_Within24Hours_WithPumpSerial_PumpAndEndIdAreNull(pumpType: InterfaceIDs.PumpType, pumpSerial: String, timestamp: Long, endId: Long): TemporaryBasal?

}