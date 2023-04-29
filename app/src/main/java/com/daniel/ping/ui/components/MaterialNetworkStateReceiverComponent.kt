package com.daniel.ping.ui.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniel.ping.R
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.White

/**
 * Composable function that displays a "No internet access" layout if the device is not connected to an active network
 * @param modifier The modifier to customize the layout of the composable
 */
@Composable
fun NetworkStateReceiverComponent(
    modifier: Modifier
) {
    // Gets the current context using the LocalContext.current method.
    val context = LocalContext.current

    // Remembers the current network connection state with a mutable state variable
    val isConnected = remember { mutableStateOf(checkIsConnected(context)) }

    DisposableEffect(Unit){
        // Creates an intent filter to listen for connectivity changes using the CONNECTIVITY_ACTION action
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

        // Creates a broadcast receiver to handle connectivity changes and updates the state variable accordingly
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                isConnected.value = checkIsConnected(context)
            }

        }

        // Registers the broadcast receiver to listen for connectivity changes using the intent filter
        context.registerReceiver(broadcastReceiver, intentFilter)

        // Unregisters the broadcast receiver when the composable is disposed of
        onDispose {
            context.unregisterReceiver(broadcastReceiver)
        }
    }

    // Displays the "No internet access" layout if the device is not connected to an active network
    if (!isConnected.value) NoInternetAccessLayout(modifier = modifier)

}

/**
 * This function checks whether the device is connection to an active internet network
 * It takes the application context as a parameter to obtain the connectivity service
 * @param context The application context
 * @return True if there is an active network and it is connected or in the process of connecting, otherwise false
 */
private fun checkIsConnected(context: Context): Boolean{
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

@Composable
fun NoInternetAccessLayout(
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .background(Onyx),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_wifi_off),
            contentDescription = stringResource(id = R.string.youDontHaveInternetAccess),
            tint = White,
            modifier = Modifier
                .size(40.dp)
                .padding(start = 10.dp, end = 10.dp)
        )

        Text(
            text = stringResource(id = R.string.youDontHaveInternetAccess),
            color = White,
            fontFamily = FontFamily(Font(R.font.roboto)),
            fontSize = 14.sp,
            maxLines = 1
        )

    }
}