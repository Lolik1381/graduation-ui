//package ru.stankin.compose.presentation.main
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.material.Divider
//import androidx.compose.material.Icon
//import androidx.compose.material.Text
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CalendarMonth
//import androidx.compose.material.icons.filled.Done
//import androidx.compose.material.icons.filled.Task
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.em
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import ru.stankin.compose.R
//import ru.stankin.compose.core.util.Route
//import ru.stankin.compose.presentation.theme.GrayD1
//
//@Preview(showBackground = true)
//@Composable
//fun CreateAdminPage(
//    navController: NavController = rememberNavController()
//) {
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = stringResource(id = R.string.create_admin_page),
//            modifier = Modifier
//                .padding(top = 15.dp, bottom = 5.dp),
//            fontSize = 4.em,
//            fontWeight = FontWeight.W500,
//            textAlign = TextAlign.Center
//        )
//
//        CreateAdminPageItem(
//            imageVector = Icons.Default.Done,
//            description = "Шаблон задания",
//            onClick = {
//                navController.navigate(Route.TASK_TEMPLATE_CREATE.path) {
//                    navController.graph.startDestinationRoute?.let { screenRoute ->
//                        popUpTo(screenRoute) {
//                            saveState = true
//                        }
//                    }
//
//                    launchSingleTop = true
//                    restoreState = true
//                }
//            }
//        )
//    }
//}
//
//@Composable
//fun CreateAdminPageItem(
//    imageVector: ImageVector,
//    description: String,
//    onClick: () -> Unit
//) {
//    Column(
//        modifier = Modifier.padding(bottom = 8.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(start = 12.dp, bottom = 2.dp)
//                .clickable { onClick.invoke() }
//                .fillMaxWidth(),
//        ) {
//            Icon(
//                imageVector = imageVector,
//                contentDescription = ""
//            )
//
//            Spacer(Modifier.width(8.dp))
//            Text(text = description)
//        }
//        Divider(startIndent = 12.dp, thickness = 1.dp, color = GrayD1)
//    }
//}