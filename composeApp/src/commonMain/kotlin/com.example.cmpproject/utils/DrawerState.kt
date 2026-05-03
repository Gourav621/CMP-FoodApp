package com.example.cmpproject.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable

fun DrawerItem(text: String,icons: ImageVector,onItemClick: () -> Unit){
    Row (modifier = Modifier.fillMaxWidth().clickable{
        onItemClick()
    } .padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically){
        Icon(imageVector = icons, contentDescription = null,
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = Color.White,
            fontSize = 16.sp)
    }

}