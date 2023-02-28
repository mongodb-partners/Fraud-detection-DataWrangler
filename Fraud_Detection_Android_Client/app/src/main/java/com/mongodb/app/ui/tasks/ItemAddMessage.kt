package com.mongodb.app.ui.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mongodb.app.data.MockRepository
import com.mongodb.app.presentation.tasks.ItemAddMessageViewModel
import com.mongodb.app.ui.theme.MyApplicationTheme
import com.mongodb.app.ui.theme.Purple200

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemAddMessagePrompt(viewModel: ItemAddMessageViewModel) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            viewModel.closeMessageDialog()
        },
        text = {
            Column {
                Text(
                    text = viewModel.trxMessage.value
                )
            }
        },
        confirmButton = {
            Button(
                colors = buttonColors(containerColor = Purple200),
                onClick = {
                    viewModel.closeMessageDialog()
                }
            ) {
                Text("Okay")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ItemAddMessage() {
    MyApplicationTheme {
        MyApplicationTheme {
            val repository = MockRepository()
            val viewModel = ItemAddMessageViewModel(repository)
            ItemAddMessagePrompt(viewModel)
        }
    }
}
