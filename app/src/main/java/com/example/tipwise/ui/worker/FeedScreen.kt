package com.example.tipwise.ui.worker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tipwise.models.WorkerResponse
import com.example.tipwise.ui.theme.PacificBridge

@Composable
fun FeedScreen(
    viewModel: WorkerViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val workersState by viewModel.workerLiveData.observeAsState()
    val workers = workersState?.data ?: emptyList()

    LaunchedEffect(key1 = true) {
        viewModel.getWorkers()
    }

    val tempWorker = WorkerResponse(
        __v = 5559,
        _id = "sea",
        bankAccountName = "Kathie Cooper",
        bankAccountNumber = "omittam",
        ifscCode = "facilis",
        name = "Nathan Arnold",
        photo = "https://www.gravatar.com/avatar/2c7d99fe281ecd3bcd65ab915bac6dd5?s=250",
        profession = "latine",
        upiId = "donec",
        userId = "ubique"
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = FloatingActionButtonDefaults.shape,
                modifier = Modifier.padding(bottom = 10.dp, end = 10.dp),
                containerColor = PacificBridge,
                contentColor = Color.White,
                onClick = { navController.navigate("add_worker") }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ){
            Text(
                text = "Your valuable team!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(12.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                items(workers) { worker ->
                    WorkerItem(
                        worker = worker,
                        onWorkerClick = { navController.navigate("add_worker/${worker._id}") }
                    )
                }

            }
        }


//        Column(
//            modifier = Modifier.padding(it)
//        ) {
//            WorkerItem(
//                worker = tempWorker,
//                onWorkerClick = { navController.navigate("add_worker/${tempWorker._id}") }
//            )
//        }


    }
}

@Composable
fun WorkerItem(
    worker: WorkerResponse,
    onWorkerClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(200.dp)
            .padding(10.dp)
            .clickable { onWorkerClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(worker.photo)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Text(
                text = worker.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}