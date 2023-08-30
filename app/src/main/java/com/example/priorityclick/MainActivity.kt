package com.example.priorityclick

import android.annotation.SuppressLint
import android.content.Context

import android.os.Build
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.dataStore
import androidx.datastore.*
import androidx.datastore.core.DataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.priorityclick.profile.ProfileScreen
import com.example.priorityclick.sign_in.GoogleAuthUiClient
import com.example.priorityclick.sign_in.SignInScreen
import com.example.priorityclick.sign_in.SignInViewModel
import com.example.priorityclick.ui.theme.LightGreen
import com.example.priorityclick.ui.theme.PriorityClickTheme
import com.example.priorityclick.ui.theme.SuperDarkGreen
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch
import com.example.priorityclick.ui.theme.DarkRed
import com.google.android.gms.auth.api.identity.Identity


val Context.dataStore by dataStore("my_data.json", TaskSerializer)

class MainActivity : ComponentActivity() {
    var list_: List<Task> = mutableListOf()

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PriorityClickTheme {
                val navController = rememberNavController()
                list_ = dataStore.data.collectAsState(
                    initial = TaskList()
                ).value.list
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem(
                                    name = "Home",
                                    route = "home",
                                    icon = ImageVector.vectorResource(id = R.drawable.event_available)
                                ),
                                BottomNavItem(
                                    name = "Add",
                                    route = "add",
                                    icon = ImageVector.vectorResource(id = R.drawable.add_circle)
                                ),
                                BottomNavItem(
                                    name = "Settings",
                                    route = "profile",
                                    icon = ImageVector.vectorResource(id = R.drawable.person)
                                ),
                            ),
                            controller = navController,
                            OnItemClick = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                ) {
                    Navigation(navController = navController)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun TasksScreen(navController: NavHostController) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        ) {
            if (list_.size > 0) {
                Column(horizontalAlignment = CenterHorizontally) {
                    Text(
                        text = "My deadlines",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(top = 30.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    val scope = rememberCoroutineScope()
                    LazyColumn() {
                        var i: Int = 0
                        for (t in list_) {
                            item {
                                TaskCard(task = t, i)
                                ++i
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            } else {
                Column(horizontalAlignment = CenterHorizontally) {
                    Text(
                        text = "My deadlines",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(top = 30.dp, bottom = 20.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.fillMaxHeight(0.2f))
                    Text(
                        text = "You don't have any deadlines \uD83E\uDD73",
                        fontSize = 25.sp,
                        modifier = Modifier
                            .padding(top = 30.dp, bottom = 20.dp)
                            .width(250.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Card(
                        modifier = Modifier
                            .height(50.dp)
                            .width(200.dp)
                            .clickable { navController.navigate("add") },
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Add deadline",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 30.dp)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.add_circle),
                                contentDescription = "Add",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AddTaskScreen(navController: NavHostController) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary

        ) {
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier.padding(vertical = 0.dp)
            ) {
                Text(
                    text = "Create deadline!",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 30.dp, bottom = 60.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                var title by remember { mutableStateOf("") }
                var title_incorrect by remember { mutableStateOf(false) }
                val focusManager = LocalFocusManager.current
                Column() {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .onFocusChanged { if (it.isFocused) title_incorrect = false },
                        value = title,
                        onValueChange = {
                            title = if (it.length < 50) it else title
                        },
                        label = {
                            Text(
                                "Title",
                                color = if (title_incorrect) DarkRed else Color.Unspecified
                            )
                        },
                        isError = title_incorrect,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.DarkGray
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.large,
                    )
                    if (title_incorrect) {
                        Text(
                            text = "Please, enter correct title",
                            color = DarkRed,
                            modifier = Modifier.padding(start = 20.dp, top = 5.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                var desc by remember { mutableStateOf("") }
                var desc_incorrect by remember { mutableStateOf(false) }
                val interactionSource = MutableInteractionSource()
                Column() {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .onFocusChanged { if (it.isFocused) desc_incorrect = false },
                        value = desc,
                        onValueChange = {
                            desc = if (it.length < 300) it else desc
                        },
                        label = {
                            Text(
                                "Description",
                                color = if (desc_incorrect) DarkRed else Color.Unspecified
                            )
                        },
                        isError = desc_incorrect,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.DarkGray
                        ),
                        shape = MaterialTheme.shapes.large,
                    )
                    if (desc_incorrect) {
                        Text(
                            text = "Please, enter correct description",
                            color = DarkRed,
                            modifier = Modifier.padding(start = 20.dp, top = 5.dp)
                        )
                    }
                }
                var pickedDate by remember {
                    mutableStateOf(LocalDate.now())
                }
                val formattedDate by remember {
                    derivedStateOf {
                        DateTimeFormatter
                            .ofPattern("MMM dd yyyy")
                            .format(pickedDate)
                    }
                }
                val dateDialogState = rememberMaterialDialogState()
                Spacer(modifier = Modifier.height(40.dp))

                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.tune),
                            contentDescription = "",
                            tint = Color.DarkGray,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    dateDialogState.show()
                                }
                                .height(60.dp)
                                .width(60.dp)
                                .padding(top = 10.dp, bottom = 10.dp, start = 15.dp, end = 15.dp)
                        )
                    },
                    value = formattedDate,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold
                    ),
                    onValueChange = {},
                    label = { Text("Date") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.DarkGray
                    ),
                    shape = MaterialTheme.shapes.large,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Spacer(modifier = Modifier.height(60.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(60.dp)
                        .clickable {
                            focusManager.clearFocus()
                            if (title.length > 0 && desc.length > 0) {
                                scope.launch {
                                    AddCard(title = title, desc = desc, date = pickedDate)
                                    title = ""
                                    desc = ""
                                }
                                navController.navigate("home")
                            } else {
                                if (title.length == 0) {
                                    title_incorrect = true
                                }
                                if (desc.length == 0) {
                                    desc_incorrect = true
                                }
                            }
                        }, shape = MaterialTheme.shapes.large
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                        Text(text = "SUBMIT", fontSize = 18.sp, modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold)
                    }
                }
                MaterialDialog(
                    shape = MaterialTheme.shapes.large,
                    dialogState = dateDialogState,
                    buttons = {
                        positiveButton(
                            text = "OK",
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)
                        ) {
                        }
                        negativeButton(
                            text = "Cancel",
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)
                        )
                    },
                ) {
                    datepicker(
                        initialDate = LocalDate.now(),
                        //title = "Pick a date",
                        allowedDateValidator = {
                            it >= LocalDate.now()
                        },
                        colors = DatePickerDefaults.colors(
                            headerBackgroundColor = MaterialTheme.colorScheme.primary,
                            dateActiveBackgroundColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        pickedDate = it
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun AddCard(title: String, desc: String, date: LocalDate) {
        list_ = list_.plus(
            Task(
                title = title,
                desc = desc,
                date = MyDate(date.dayOfMonth, date.monthValue, date.year)
            )
        )
        list_ = list_.sortedWith(compareBy({ it.date.year }, { it.date.month }, { it.date.day }))
        dataStore.updateData {
            it.copy(list = list_)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun DelCard(k: Int) {
        var list1_: List<Task> = mutableListOf()
        for (i in 0..(list_.size - 1)) {
            if (i != k) {
                list1_ = list1_.plus(list_[i])
            }
        }
        list_ = list1_
        dataStore.updateData {
            it.copy(list = list_)
        }
    }

    @Composable
    fun BottomNavigationBar(
        items: List<BottomNavItem>,
        controller: NavController,
        OnItemClick: (BottomNavItem) -> Unit
    ) {

        val backStackEntry = controller.currentBackStackEntryAsState()

        BottomNavigation(
            backgroundColor = SuperDarkGreen,
            elevation = 5.dp
        ) {
            items.forEach { item ->
                val selected =
                    item.route == backStackEntry.value?.destination?.route || (item.route == "profile" && backStackEntry.value?.destination?.route == "sign_in")
                BottomNavigationItem(
                    selected = selected,
                    onClick = { OnItemClick(item) },
                    selectedContentColor = LightGreen,
                    unselectedContentColor = Color.White,
                    icon = {
                        Column(horizontalAlignment = CenterHorizontally) {
                            var col: Color
                            if (selected) {
                                col = Color.White
                            } else {
                                col = Color.Black
                            }
                            Icon(
                                modifier = Modifier.padding(top = 15.dp, bottom = 0.dp),
                                imageVector = item.icon,
                                contentDescription = item.name,
                                tint = col
                            )
                        }
                    }
                )
            }
        }
    }

//    @Composable
//    fun MyProfileScreen() {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.primary
//
//        ) {
//            Column(
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = CenterHorizontally
//            ) {
//                Text(text = "PROFILE", fontSize = 30.sp)
//            }
//        }
//    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun TaskCardPreview() {
        PriorityClickTheme {
            TaskCard(
                task = Task(
                    "Android",
                    "I\nNeed\nTo\nFinish\nmy\nApp",
                    MyDate(20, 8, 2023)
                ), 0
            )
            //AddTaskScreen()
        }
    }


    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun TaskCard(task: Task, i: Int) {
        var openDialog by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        var not_deleted by remember { mutableStateOf(true) }
        val interactionSource = MutableInteractionSource()
        var status by remember { mutableStateOf(false) }
        val rotateState = animateFloatAsState(
            targetValue = if (status) 180F else 0F,
        )
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(10.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { status = !status },
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 30.dp, end = 30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(),
                        text = task.title,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Icon(
                        painterResource(R.drawable.expand_more), "",
                        modifier = Modifier
                            .rotate(rotateState.value)
                            .height(40.dp)
                            .width(40.dp)
                    )
                }
                //Spacer(modifier = Modifier.height(20.dp))
                //Divider()
                AnimatedVisibility(visible = status) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, bottom = 20.dp, start = 50.dp, end = 40.dp)
                    ) {
                        Text(text = task.desc, fontSize = 16.sp)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = task.date.toString(),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 30.dp, vertical = 15.dp),
                        fontWeight = FontWeight.Bold
                    )

                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .width(70.dp)
                            .padding(end = 30.dp)
                            .clickable {
                                openDialog = true
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                            Icon(
                                painter = painterResource(id = R.drawable.done),
                                contentDescription = "Done",
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(60.dp)
                            )
                        }
                    }
                }

            }
            if (openDialog) {

                AlertDialog(
                    modifier = Modifier
                        .height(130.dp)
                        .width(250.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    onDismissRequest = {
                        openDialog = false
                    },
                    title = {
                        Text(
                            text = "Close deadline?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp, start = 10.dp)
                        )
                    },
                    confirmButton = {
                        Button(
                            modifier = Modifier.padding(end = 20.dp, top = 10.dp, bottom = 10.dp),
                            onClick = {
                                if (i != 129) {
                                    not_deleted = false
                                    scope.launch {
                                        DelCard(i)
                                    }
                                }
                                openDialog = false
                            }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                            onClick = {
                                openDialog = false
                            }) {
                            Text("CANCEL")
                        }
                    }
                )
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                TasksScreen(navController = navController)
            }
            composable("add") {
                AddTaskScreen(navController = navController)
            }
            composable("sign_in") {
                val viewModel = viewModel<SignInViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        navController.navigate("profile")
                        viewModel.resetState()
                    }
                }

                SignInScreen(
                    state = state,
                    onSignInClick = {
                        lifecycleScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                )
            }
            composable("profile") {
                LaunchedEffect(key1 = Unit) {
                    if (googleAuthUiClient.getSignedInUser() == null) {
                        navController.navigate("sign_in")
                    }
                }
                ProfileScreen(
                    userData = googleAuthUiClient.getSignedInUser(),
                    onSignOut = {
                        lifecycleScope.launch {
                            googleAuthUiClient.signOut()

                            navController.navigate("sign_in")
                        }
                    }
                )
            }
        }
    }
}

private suspend fun Biba(d: DataStore<TaskList>) {

}



