package ts.tally.document_scanner.fallback.extensions

import android.annotation.SuppressLint
import androidx.appcompat.view.menu.MenuItemImpl
import com.google.android.material.bottomnavigation.BottomNavigationView

@SuppressLint("RestrictedApi")
fun BottomNavigationView.deselectAllItems() {
    val menu = this.menu

    for (i in 0 until menu.size()) {
        (menu.getItem(i) as? MenuItemImpl)?.let {
            it.isExclusiveCheckable = false
            it.isChecked = false
            it.isExclusiveCheckable = true
        }
    }
}