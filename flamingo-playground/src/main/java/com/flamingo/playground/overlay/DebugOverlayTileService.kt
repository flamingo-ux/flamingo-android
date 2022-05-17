package com.flamingo.playground.overlay

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.flamingo.Flamingo
import com.flamingo.utils.SDK_24

@RequiresApi(SDK_24)
class DebugOverlayTileService : TileService() {
    @RequiresApi(SDK_24)
    override fun onClick() {
        super.onClick()
        with(Flamingo) {
            qsTile.state = if (debugOverlay == null) {
                enableDebugOverlay()
                Tile.STATE_ACTIVE
            } else {
                disableDebugOverlay()
                Tile.STATE_INACTIVE
            }
            qsTile.updateTile()
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        qsTile.state =
            if (Flamingo.debugOverlay == null) Tile.STATE_INACTIVE else Tile.STATE_ACTIVE
        qsTile.updateTile()
    }
}
