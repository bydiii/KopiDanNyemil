package com.example.kopidannyemil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

// Palette Warna Premium Coffee Aesthetic
private val CreamBg = Color(0xFFF7F3E9)        // Background Cream Hangat
private val CardBg = Color(0xFFFFFFFF)         // Putih bersih untuk kartu
private val CoffeeCherry = Color(0xFF9E2A2B)   // Merah Cherry Aksen Utama
private val CoffeeRoast = Color(0xFF3D2314)    // Cokelat Gelap untuk Teks Utama
private val CaramelGold = Color(0xFFD48C46)    // Emas Karamel untuk Highlight & Aksen
private val SoftCream = Color(0xFFEFE4D1)      // Cream Muda untuk Chip/Filter
private val MutedText = Color(0xFF8C7A6B)      // Cokelat Pudar untuk Deskripsi
private val SuccessGreen = Color(0xFFA3E4B7)   // Hijau Lembut Lingkaran OK (sesuai desain)

private val TitleFont = FontFamily.Serif
private val BodyFont = FontFamily.SansSerif

data class MenuItem(
    val name: String,
    val detail: String,
    val price: Int,
    val emoji: String,
    val category: String
)

private val foodMenus = listOf(
    MenuItem("Croissant", "Pastry renyah & butter gurih", 18000, "🥐", "Makanan"),
    MenuItem("Donat Cokelat", "Glaze cokelat belgian empuk", 12000, "🍩", "Makanan"),
    MenuItem("Sandwich", "Roti isi daging & sayur segar", 22000, "🥪", "Makanan"),
    MenuItem("Kentang Goreng", "Kentang renyah bumbu herbs", 15000, "🍟", "Makanan"),
    MenuItem("Brownies", "Brownies cokelat lumer premium", 17000, "🍫", "Makanan")
)

private val drinkMenus = listOf(
    MenuItem("Kopi Gula Aren", "Espresso, fresh milk & aren", 22000, "🧋", "Minuman"),
    MenuItem("Cappuccino", "Espresso lembut & foam tebal", 25000, "☕", "Minuman"),
    MenuItem("Matcha Latte", "Matcha jepang & artisan milk", 27000, "🍵", "Minuman"),
    MenuItem("Es Cokelat", "Cokelat pekat dingin creamy", 20000, "🥤", "Minuman"),
    MenuItem("Teh Hangat", "Teh chamomile menenangkan", 15000, "🫖", "Minuman")
)

private val allMenus = foodMenus + drinkMenus

private fun rupiah(amount: Int): String {
    return NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(amount)
        .replace(",00", "")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KopiApp()
        }
    }
}

@Composable
fun KopiApp() {
    var page by remember { mutableStateOf("Splash") }
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var addressDetail by remember { mutableStateOf("") }
    var cart by remember { mutableStateOf(listOf<MenuItem>()) }
    var message by remember { mutableStateOf("") }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = CoffeeCherry,
            secondary = CaramelGold,
            background = CreamBg,
            surface = CardBg,
            onPrimary = Color.White,
            onBackground = CoffeeRoast
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = CreamBg
        ) {
            when (page) {
                "Splash" -> SplashScreen { page = "Welcome" }
                "Welcome" -> WelcomeScreen(
                    onRegister = { page = "Register" },
                    onLogin = { page = "Login" }
                )
                "Register" -> AuthScreen(
                    title = "Buat Akun Baru ✨",
                    subtitle = "Mulai petualangan rasa kopimu bersama kami",
                    showName = true,
                    name = name,
                    username = username,
                    password = password,
                    onName = { name = it },
                    onUsername = { username = it },
                    onPassword = { password = it },
                    buttonText = "Daftar Sekarang",
                    secondaryText = "Sudah punya akun? Login di sini",
                    onSubmit = {
                        if (name.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
                            message = "Akun berhasil dibuat. Halo, $name!"
                            page = "Home"
                        } else {
                            message = "Harap lengkapi semua data."
                        }
                    },
                    onSecondary = { page = "Login" }
                )
                "Login" -> AuthScreen(
                    title = "Selamat Datang ☕",
                    subtitle = "Masuk untuk menikmati kopi favoritmu",
                    showName = false,
                    name = name,
                    username = username,
                    password = password,
                    onName = { name = it },
                    onUsername = { username = it },
                    onPassword = { password = it },
                    buttonText = "Masuk",
                    secondaryText = "Belum punya akun? Register di sini",
                    onSubmit = {
                        if (username.isNotBlank() && password.isNotBlank()) {
                            message = "Selamat datang kembali, ${name.ifBlank { username }}!"
                            page = "Home"
                        } else {
                            message = "Isi username dan password dahulu."
                        }
                    },
                    onSecondary = { page = "Register" }
                )
                "Home" -> HomeScreen(
                    name = name.ifBlank { username.ifBlank { "Teman Kopi" } },
                    cartCount = cart.size,
                    message = message,
                    onAdd = { item ->
                        cart = cart + item
                        message = "${item.name} ditambahkan."
                    },
                    onCart = { page = "Cart" },
                    onProfile = { page = "Profile" }
                )
                "Cart" -> CartScreen(
                    cart = cart,
                    onIncrease = { item -> cart = cart + item },
                    onDecrease = { item ->
                        val updatedCart = cart.toMutableList()
                        val index = updatedCart.indexOf(item)
                        if (index >= 0) updatedCart.removeAt(index)
                        cart = updatedCart
                    },
                    onRemove = { item -> cart = cart.filterNot { it.name == item.name } },
                    onCheckout = { if (cart.isNotEmpty()) page = "Address" },
                    onBack = { page = "Home" }
                )
                "Profile" -> ProfileScreen(
                    name = name,
                    username = username,
                    onName = { name = it },
                    onSave = {
                        message = "Profil diperbarui."
                        page = "Home"
                    },
                    onHome = { page = "Home" },
                    onCart = { page = "Cart" }
                )
                "Address" -> AddressScreen(
                    name = name.ifBlank { username },
                    address = address,
                    detail = addressDetail,
                    cartCount = cart.size,
                    onAddress = { address = it },
                    onDetail = { addressDetail = it },
                    onSubmit = { if (address.isNotBlank()) page = "Confirm" },
                    onBack = { page = "Cart" }
                )
                "Confirm" -> ConfirmScreen(
                    name = name.ifBlank { username.ifBlank { "Teman Kopi" } },
                    cart = cart,
                    address = address,
                    detail = addressDetail,
                    onDone = {
                        // Pindah ke Halaman Success Akhir
                        page = "Success"
                    }
                )
                "Success" -> SuccessScreen(
                    name = name.ifBlank { username.ifBlank { "Diah" } },
                    onFinish = {
                        cart = emptyList()
                        address = ""
                        addressDetail = ""
                        message = "Pesanan sedang diproses!"
                        page = "Home"
                    }
                )
            }
        }
    }
}

// Custom Top Bar dengan Tombol Back Estetik
@Composable
fun AestheticTopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(42.dp)
                .background(CardBg, CircleShape)
                .shadow(2.dp, CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = CoffeeRoast)
        }
        Spacer(Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TitleFont,
            color = CoffeeRoast
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(4.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CoffeeCherry)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            fontFamily = BodyFont
        )
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    secret: Boolean = false,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label, color = MutedText, fontSize = 14.sp) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = singleLine,
        visualTransformation = if (secret) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CoffeeCherry,
            unfocusedBorderColor = SoftCream,
            focusedContainerColor = CardBg,
            unfocusedContainerColor = CardBg,
            focusedTextColor = CoffeeRoast,
            unfocusedTextColor = CoffeeRoast
        )
    )
}

@Composable
fun SplashScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(SoftCream, CircleShape)
                .border(2.dp, CaramelGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("☕", fontSize = 65.sp)
        }
        Spacer(Modifier.height(28.dp))
        Text(
            "Kopi & Nyemil",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TitleFont,
            color = CoffeeRoast
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Artisan Coffee & Bakery Experience",
            fontSize = 14.sp,
            color = MutedText,
            fontFamily = BodyFont
        )
        Spacer(Modifier.height(50.dp))
        PrimaryButton("Jelajahi Menu", onContinue)
    }
}

@Composable
fun WelcomeScreen(
    onRegister: () -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
            .padding(28.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.height(20.dp))
        Column {
            Text("📍 Cafe Neighborhood", fontSize = 13.sp, color = CaramelGold, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Rasakan Momen\nNgopi Terbaik.",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TitleFont,
                color = CoffeeRoast,
                lineHeight = 42.sp
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Nikmati seduhan kopi manis dan pastry segar yang dibuat penuh cinta setiap hari.",
                color = MutedText,
                fontSize = 15.sp,
                fontFamily = BodyFont
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            PrimaryButton("Buat Akun Baru", onRegister)
            Spacer(Modifier.height(14.dp))
            OutlinedButton(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CoffeeRoast)
            ) {
                Text("Masuk ke Akun", color = CoffeeRoast, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun AuthScreen(
    title: String,
    subtitle: String,
    showName: Boolean,
    name: String,
    username: String,
    password: String,
    onName: (String) -> Unit,
    onUsername: (String) -> Unit,
    onPassword: (String) -> Unit,
    buttonText: String,
    secondaryText: String,
    onSubmit: () -> Unit,
    onSecondary: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(Modifier.height(30.dp))
        Text(title, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = TitleFont, color = CoffeeRoast)
        Spacer(Modifier.height(6.dp))
        Text(subtitle, color = MutedText, fontSize = 14.sp)

        Spacer(Modifier.height(32.dp))

        if (showName) {
            InputField("Nama Lengkap", name, onName)
            Spacer(Modifier.height(16.dp))
        }

        InputField("Username", username, onUsername)
        Spacer(Modifier.height(16.dp))
        InputField("Password", password, onPassword, secret = true)

        Spacer(Modifier.height(32.dp))
        PrimaryButton(buttonText, onSubmit)

        Spacer(Modifier.height(12.dp))
        TextButton(
            onClick = onSecondary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(secondaryText, color = CoffeeCherry, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun HomeScreen(
    name: String,
    cartCount: Int,
    message: String,
    onAdd: (MenuItem) -> Unit,
    onCart: () -> Unit,
    onProfile: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Semua") }
    val categories = listOf("Semua", "Makanan", "Minuman")

    val filteredMenus = when (selectedCategory) {
        "Makanan" -> foodMenus
        "Minuman" -> drinkMenus
        else -> allMenus
    }

    Box(modifier = Modifier.fillMaxSize().background(CreamBg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Selamat datang 👋", fontSize = 13.sp, color = MutedText)
                    Text(
                        name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = TitleFont,
                        color = CoffeeRoast
                    )
                }
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(CardBg)
                        .border(1.dp, SoftCream, CircleShape)
                        .clickable { onProfile() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = CoffeeRoast)
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = CoffeeRoast)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("PROMO HARI INI", fontSize = 11.sp, color = CaramelGold, fontWeight = FontWeight.Bold)
                        Text("Diskon 20% Kopi Aren", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Gunakan voucher: NGOPIHAPPY", fontSize = 11.sp, color = SoftCream)
                    }
                    Text("☕✨", fontSize = 38.sp)
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) CoffeeCherry else CardBg)
                            .border(1.dp, if (isSelected) CoffeeCherry else SoftCream, RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.White else CoffeeRoast,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (message.isNotBlank()) {
                Text(
                    message,
                    fontSize = 12.sp,
                    color = CoffeeCherry,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 90.dp)
            ) {
                items(filteredMenus) { item ->
                    ModernMenuCard(item = item, onAdd = { onAdd(item) })
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                color = CoffeeRoast
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onProfile() }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = SoftCream)
                        Spacer(Modifier.width(6.dp))
                        Text("Profil", color = SoftCream, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    Button(
                        onClick = onCart,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CoffeeCherry),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Keranjang ($cartCount)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ModernMenuCard(
    item: MenuItem,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CreamBg),
                contentAlignment = Alignment.Center
            ) {
                Text(item.emoji, fontSize = 42.sp)
            }

            Spacer(Modifier.height(10.dp))
            Text(
                item.name,
                fontWeight = FontWeight.Bold,
                fontFamily = TitleFont,
                fontSize = 15.sp,
                color = CoffeeRoast,
                maxLines = 1
            )
            Text(
                item.detail,
                fontSize = 11.sp,
                color = MutedText,
                maxLines = 2,
                lineHeight = 14.sp
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    rupiah(item.price),
                    fontWeight = FontWeight.Bold,
                    color = CoffeeCherry,
                    fontSize = 13.sp
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(CoffeeCherry)
                        .clickable { onAdd() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    cart: List<MenuItem>,
    onIncrease: (MenuItem) -> Unit,
    onDecrease: (MenuItem) -> Unit,
    onRemove: (MenuItem) -> Unit,
    onCheckout: () -> Unit,
    onBack: () -> Unit
) {
    val groupedCart = cart.groupBy { it.name }
    val total = cart.sumOf { it.price }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
            .padding(20.dp)
    ) {
        AestheticTopBar(title = "Keranjang Saya", onBack = onBack)
        Text("${cart.size} item siap dipesan", color = MutedText, fontSize = 13.sp)

        Spacer(Modifier.height(16.dp))

        if (cart.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛍️", fontSize = 65.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Keranjang Masih Kosong",
                        fontWeight = FontWeight.Bold,
                        fontFamily = TitleFont,
                        fontSize = 18.sp,
                        color = CoffeeRoast
                    )
                    Text("Yuk, tambah menu favoritmu dulu!", color = MutedText, fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(groupedCart.values.toList()) { sameItems ->
                    val item = sameItems.first()
                    val quantity = sameItems.size

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.emoji, fontSize = 34.sp)
                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Bold, color = CoffeeRoast, fontSize = 15.sp)
                                Text(rupiah(item.price), color = CaramelGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(SoftCream)
                                        .clickable { onDecrease(item) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("−", color = CoffeeRoast, fontWeight = FontWeight.Bold)
                                }

                                Text(
                                    quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = CoffeeRoast
                                )

                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(CoffeeCherry)
                                        .clickable { onIncrease(item) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(Modifier.width(12.dp))
                            IconButton(onClick = { onRemove(item) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = CoffeeCherry)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Pembayaran", fontSize = 12.sp, color = MutedText)
                        Text(rupiah(total), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = CoffeeCherry, fontFamily = TitleFont)
                    }
                    Button(
                        onClick = onCheckout,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CoffeeCherry)
                    ) {
                        Text("Checkout", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    name: String,
    username: String,
    onName: (String) -> Unit,
    onSave: () -> Unit,
    onHome: () -> Unit,
    onCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
            .padding(24.dp)
    ) {
        AestheticTopBar(title = "Profil Pengguna", onBack = onHome)
        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(SoftCream, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 35.sp)
                }
                Spacer(Modifier.height(12.dp))
                Text(name.ifBlank { username.ifBlank { "Teman Kopi" } }, fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = TitleFont, color = CoffeeRoast)
                Text("@${username.ifBlank { "user" }}", fontSize = 13.sp, color = MutedText)
            }
        }

        Spacer(Modifier.height(24.dp))
        InputField("Ubah Nama Lengkap", name, onName)

        Spacer(Modifier.height(24.dp))
        PrimaryButton("Simpan Perubahan", onSave)
    }
}

@Composable
fun AddressScreen(
    name: String,
    address: String,
    detail: String,
    cartCount: Int,
    onAddress: (String) -> Unit,
    onDetail: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
            .padding(24.dp)
    ) {
        AestheticTopBar(title = "Pengiriman", onBack = onBack)
        Spacer(Modifier.height(16.dp))

        InputField("Nama Penerima", name, {})
        Spacer(Modifier.height(14.dp))
        InputField("Alamat Lengkap", address, onAddress)
        Spacer(Modifier.height(14.dp))
        InputField("Detail / Patokan Alamat", detail, onDetail, singleLine = false)

        Spacer(Modifier.height(30.dp))
        PrimaryButton("Lanjut ke Konfirmasi", onSubmit)
    }
}

@Composable
fun ConfirmScreen(
    name: String,
    cart: List<MenuItem>,
    address: String,
    detail: String,
    onDone: () -> Unit
) {
    val total = cart.sumOf { it.price }
    val groupedCart = cart.groupBy { it.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(20.dp))
        Text("Ringkasan Pesanan", fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = TitleFont, color = CoffeeRoast)
        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Item Dipesan", fontWeight = FontWeight.Bold, color = CoffeeRoast, fontSize = 14.sp)
                Spacer(Modifier.height(10.dp))
                groupedCart.values.forEach { sameItems ->
                    val item = sameItems.first()
                    val qty = sameItems.size
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.emoji} ${item.name} (x$qty)", fontSize = 13.sp, color = CoffeeRoast)
                        Text(rupiah(item.price * qty), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CaramelGold)
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 12.dp), color = SoftCream)
                Text("Alamat Pengiriman", fontWeight = FontWeight.Bold, color = CoffeeRoast, fontSize = 14.sp)
                Text(address, fontSize = 13.sp, color = MutedText)
                if (detail.isNotBlank()) Text(detail, fontSize = 12.sp, color = MutedText)

                HorizontalDivider(Modifier.padding(vertical = 12.dp), color = SoftCream)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Akhir", fontWeight = FontWeight.Bold, color = CoffeeRoast)
                    Text(rupiah(total), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoffeeCherry, fontFamily = TitleFont)
                }
            }
        }

        Spacer(Modifier.height(30.dp))
        PrimaryButton("Konfirmasi & Bayar", onDone)
    }
}

// 8. Halaman Akhir Sesuai Gambar: Konfirmasi Pesanan (Success Screen)
@Composable
fun SuccessScreen(
    name: String,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Teks "Halo Nama,"
            Text(
                text = "Halo $name,",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TitleFont,
                color = CoffeeRoast,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(48.dp))

            // Lingkaran Hijau OK
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "OK",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
            }

            Spacer(Modifier.height(48.dp))

            // Pesan Pengiriman Kurir
            Text(
                text = "Terima kasih sudah memesan\ntunggu aja sebentar kurir kami\nsegera mengantarkan\npesananmu!",
                fontSize = 16.sp,
                color = CoffeeRoast,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }

        // Tombol Oke Hitam / Dark Coffee
        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CoffeeRoast)
        ) {
            Text(
                text = "Oke",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}