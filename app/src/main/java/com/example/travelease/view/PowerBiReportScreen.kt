package com.example.travelease.view

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.travelease.model.PowerBiEmbedResponse
import com.example.travelease.viewmodel.ReportUiState
import com.example.travelease.viewmodel.ReportViewModel
import org.json.JSONObject

@Composable
internal fun PowerBiReportScreen(title: String, reportKey: String, viewModel: ReportViewModel, onNavigateBack: () -> Unit) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(reportKey) { viewModel.cargar(reportKey) }
    Column(Modifier.fillMaxSize()) {
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(4.dp))
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            when (val current = state) {
                ReportUiState.Loading -> CircularProgressIndicator()
                is ReportUiState.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(current.message)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { viewModel.cargar(reportKey) }) { Text("Reintentar") }
                }
                is ReportUiState.Success -> PowerBiWebView(current.report, Modifier.fillMaxSize())
            }
        }
        IconButton(onClick = onNavigateBack, modifier = Modifier.padding(start = 8.dp)) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Atrás")
        }
    }
}

@Composable
private fun PowerBiWebView(report: PowerBiEmbedResponse, modifier: Modifier = Modifier) {
    val html = remember(report) { createPowerBiHtml(report) }
    AndroidView(
        modifier = modifier,
        factory = { context -> WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        } },
        update = { it.loadDataWithBaseURL("https://app.powerbi.com/", html, "text/html", "UTF-8", null) },
        onRelease = { it.destroy() }
    )
}

private fun createPowerBiHtml(report: PowerBiEmbedResponse): String {
    val id = JSONObject.quote(report.reportId)
    val embedUrl = JSONObject.quote(report.embedUrl)
    val token = JSONObject.quote(report.embedToken)
    val pageName = report.pageName?.let { JSONObject.quote(it) } ?: "null"
    val visualName = report.visualName?.let { JSONObject.quote(it) } ?: "null"
    val html =  """<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Visualizador de Power BI</title>
    
    <!-- 1. Cargar la librería oficial de Power BI Client -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/powerbi-client/2.22.3/powerbi.min.js"></script>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f6f8;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #333;
            margin-top: 0;
        }

        /* El contenedor donde se renderizará el reporte */
        #reportContainer {
            width: 100%;
            height: 700px;
            border: 1px solid #e0e0e0;
            border-radius: 4px;
        }
    </style>
</head>
<body>

    <div class="container">
        <h2>Reporte Interactivo Power BI</h2>
        
        <!-- Div contenedor del iframe de Power BI -->
        <div id="reportContainer"></div>
    </div>

    <script>
        // 2. Extraer el SDK y sus modelos
        const powerbiClient = window['powerbi-client'];

        if (!powerbiClient) {
            throw new Error('No se pudo cargar la librería powerbi-client.');
        }

        const models = powerbiClient.models;

        // 3. Configuración con tus datos de la API
        const reportConfig = {
            type: 'visual',
            tokenType: models.TokenType.Embed, // Usa Embed para tokens temporales devueltos por la API
            id: ${id},
            embedUrl: ${embedUrl},
            accessToken: ${token},
            visualName: ${visualName},
            pageName: ${pageName}, // Abre directamente la página específica
            settings: {
                panes: {
                    filters: {
                        visible: true,
                        expanded: false
                    },
                    pageNavigation: {
                        visible: true,
                        position: models.PageNavigationPosition.Bottom
                    }
                }
            }
        };

        // 4. Obtener el contenedor del DOM
        const reportContainer = document.getElementById('reportContainer');

        // 5. Inicializar el SDK de Power BI
        const powerbiService = new powerbiClient.service.Service(
            powerbiClient.factories.hpmFactory,
            powerbiClient.factories.wpmpFactory,
            powerbiClient.factories.routerFactory
        );

        // 6. Incrustar el reporte
        const report = powerbiService.embed(reportContainer, reportConfig);

        // Eventos opcionales para detectar si cargó correctamente
        report.on("loaded", function () {
            console.log("El reporte cargó con éxito.");
        });

        report.on("error", function (event) {
            console.error("Error al cargar el reporte:", event.detail);
        });
    </script>
</body>
</html>"""

    return html
}
