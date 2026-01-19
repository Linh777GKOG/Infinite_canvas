// File: app/src/main/java/com/example/miniconceptsapp/MainActivity.kt
package com.example.miniconceptsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consume
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miniconceptsapp.ui.theme.MiniConceptsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniConceptsAppTheme {
                InfiniteCanvasScreen()
            }
        }
    }
}

data class Stroke(
    val path: Path,
    val color: Color,
    val strokeWidth: Float,
    val alpha: Float = 1f
)

data class DrawingState(
    val strokes: MutableList<Stroke> = mutableListOf(),
    val currentColor: Color = Color.Black,
    val currentStrokeWidth: Float = 4f
)

@Composable
fun InfiniteCanvasScreen() {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var drawingState by remember { mutableStateOf(DrawingState()) }
    var currentPath by remember { mutableStateOf<Path?>(null) }

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.1f, 10f)
                        offset += pan
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { startOffset ->
                            currentPath = Path().apply {
                                moveTo(startOffset.x / scale - offset.x / scale, startOffset.y / scale - offset.y / scale)
                            }
                        },
                        onDrag = { change: PointerInputChange, dragAmount: Offset ->
                            change.consume()
                            currentPath?.relativeLineTo(dragAmount.x / scale, dragAmount.y / scale)
                        },
                        onDragEnd = {
                            currentPath?.let { path ->
                                val newStrokes = drawingState.strokes.toMutableList()
                                newStrokes.add(
                                    Stroke(
                                        path = path,
                                        color = drawingState.currentColor,
                                        strokeWidth = drawingState.currentStrokeWidth * density.density
                                    )
                                )
                                drawingState = drawingState.copy(strokes = newStrokes)
                            }
                            currentPath = null
                        },
                        onDragCancel = {
                            currentPath = null
                        }
                    )
                }
        ) {
            // Vẽ strokes đã hoàn thành
            drawingState.strokes.forEach { stroke ->
                drawPath(
                    path = stroke.path,
                    color = stroke.color,
                    style = Stroke(
                        width = stroke.strokeWidth / density.density / scale,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    ),
                    alpha = stroke.alpha
                )
            }

            // Vẽ nét đang vẽ
            currentPath?.let { path ->
                drawPath(
                    path = path,
                    color = drawingState.currentColor,
                    style = Stroke(
                        width = drawingState.currentStrokeWidth / scale,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }

        // Controls
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Màu sắc
            listOf(Color.Black, Color.Red, Color.Blue, Color.Green, Color.Yellow).forEach { col ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(col, CircleShape)
                        .border(2.dp, if (drawingState.currentColor == col) Color.White else Color.Transparent, CircleShape)
                        .clickable { drawingState = drawingState.copy(currentColor = col) }
                )
            }

            // Slider độ dày
            Slider(
                value = drawingState.currentStrokeWidth,
                onValueChange = { drawingState = drawingState.copy(currentStrokeWidth = it) },
                valueRange = 1f..20f,
                modifier = Modifier.width(150.dp)
            )

            // Xóa
            IconButton(onClick = { drawingState = DrawingState() }) {
                Icon(Icons.Default.Delete, contentDescription = "Clear", tint = Color.Red)
            }
        }

        // Hiển thị scale (debug)
        Text(
            text = "Scale: ${String.format("%.2f", scale)}",
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
    }
}