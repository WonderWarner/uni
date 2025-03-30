package hu.bme.aut.android.moneytracker.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Contactless
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.ui.graphics.vector.ImageVector
import hu.bme.aut.android.moneytracker.R
import hu.bme.aut.android.moneytracker.domain.model.PayType

sealed class PayTypeUi(
    val name: Int,
    val icon: ImageVector
) {
    object CreditCard: PayTypeUi(
        name = R.string.paytype_name_creditcard,
        icon = Icons.Outlined.CreditCard
    )
    object Cash: PayTypeUi(
        name = R.string.paytype_name_cash,
        icon = Icons.Outlined.MonetizationOn
    )
    object Revolut: PayTypeUi(
        name = R.string.paytype_name_revolut,
        icon = Icons.Outlined.Contactless
    )
}

fun PayTypeUi.asPayType(): PayType {
    return when(this) {
        is PayTypeUi.CreditCard -> PayType.CARD
        is PayTypeUi.Cash -> PayType.CASH
        is PayTypeUi.Revolut -> PayType.REVOLUT
    }
}

fun PayType.asPayTypeUi(): PayTypeUi {
    return when(this) {
        PayType.CARD -> PayTypeUi.CreditCard
        PayType.CASH -> PayTypeUi.Cash
        PayType.REVOLUT -> PayTypeUi.Revolut
    }
}