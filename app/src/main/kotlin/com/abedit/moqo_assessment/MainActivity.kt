package com.abedit.moqo_assessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.abedit.moqo_assessment.ui.POIMapViewUI
import com.abedit.moqo_assessment.viewmodel.ViewModelFactoryProvider
import com.abedit.moqo_assessment.viewmodel.MapViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //For the status bar color
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimary
                )
            )
        )


        val viewModel: MapViewModel by viewModels {
            ViewModelFactoryProvider.mapViewModelFactory()
        }

        setContent {
            POIMapViewUI(viewModel = viewModel)
        }
    }
}