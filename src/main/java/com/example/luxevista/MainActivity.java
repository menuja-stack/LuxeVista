package com.example.luxevista;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista.models.Room;
import com.example.luxevista.service.PreferencesManager;
import com.example.luxevista.service.RoomService;

public class MainActivity extends AppCompatActivity {
    RoomService roomService = new RoomService(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(roomService.getAllRooms().isEmpty()){
            createRooms();
        }

        // Navigate to WelcomeActivity after 3 seconds
        new Handler().postDelayed(() -> {
            PreferencesManager preferencesManager = new PreferencesManager(this);
            int userId = preferencesManager.getUserId();
            if(userId ==-1){

                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();// Finish MainActivity so it's not in the back stack
            }else{
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }

    void createRooms(){


// Room 1
        Room room1 = new Room("102", "Suite", 300.00, 3,
                "Spacious suite with garden view", true, "WiFi, TV, Kitchenette",
                "https://dq5r178u4t83b.cloudfront.net/wp-content/uploads/sites/125/2020/06/15182916/Sofitel-Dubai-Wafi-Luxury-Room-Bedroom-Skyline-View-Image1_WEB.jpg");
        long room1Id = roomService.createRoom(room1);

// Room 2
        Room room2 = new Room("103", "Standard", 150.00, 2,
                "Comfortable standard room with modern amenities", true, "WiFi, TV",
                "https://aremorch.com/wp-content/uploads/2016/09/The-Details-That-Matter-Top-Things-Every-Luxury-Hotel-Room-Should-Have.png");
        long room2Id = roomService.createRoom(room2);

// Room 3
        Room room3 = new Room("104", "Presidential Suite", 500.00, 4,
                "Elegant suite with panoramic city views", true, "WiFi, TV, Mini-bar, Jacuzzi",
                "https://assets.architecturaldigest.in/photos/65b2aecf269da4a0ee6c9b40/master/w_1600%2Cc_limit/atr.royalmansion-bedroom2-mr.jpg");
        long room3Id = roomService.createRoom(room3);

// Room 4
        Room room4 = new Room("105", "Economy", 100.00, 2,
                "Affordable room with essential features", true, "WiFi, TV",
                "https://cf.bstatic.com/xdata/images/hotel/max1024x768/25314905.jpg?k=b1cc101c72242a9ddaeaeb687a59605f4b358271d78a5a66c1e94f368d3c4595&o=&hp=1");
        long room4Id = roomService.createRoom(room4);

// Room 5
        Room room5 = new Room("106", "Deluxe", 220.00, 2,
                "Luxurious room with mountain view", true, "WiFi, TV, Mini-bar",
                "https://dq5r178u4t83b.cloudfront.net/wp-content/uploads/sites/125/2020/06/15182916/Sofitel-Dubai-Wafi-Luxury-Room-Bedroom-Skyline-View-Image1_WEB.jpg");
        long room5Id = roomService.createRoom(room5);

// Room 6
        Room room6 = new Room("107", "Family Suite", 400.00, 5,
                "Spacious family suite with multiple beds", true, "WiFi, TV, Kitchenette, Play Area",
                "https://aremorch.com/wp-content/uploads/2016/09/The-Details-That-Matter-Top-Things-Every-Luxury-Hotel-Room-Should-Have.png");
        long room6Id = roomService.createRoom(room6);

// Room 7
        Room room7 = new Room("108", "Penthouse", 700.00, 6,
                "Exquisite penthouse with private pool", true, "WiFi, TV, Mini-bar, Pool",
                "https://assets.architecturaldigest.in/photos/65b2aecf269da4a0ee6c9b40/master/w_1600%2Cc_limit/atr.royalmansion-bedroom2-mr.jpg");
        long room7Id = roomService.createRoom(room7);

// Room 8
        Room room8 = new Room("109", "Single", 120.00, 1,
                "Cozy single room for business travelers", true, "WiFi, TV",
                "https://cf.bstatic.com/xdata/images/hotel/max1024x768/25314905.jpg?k=b1cc101c72242a9ddaeaeb687a59605f4b358271d78a5a66c1e94f368d3c4595&o=&hp=1");
        long room8Id = roomService.createRoom(room8);

// Room 9
        Room room9 = new Room("110", "Junior Suite", 250.00, 3,
                "Stylish junior suite with premium amenities", true, "WiFi, TV, Mini-bar",
                "https://dq5r178u4t83b.cloudfront.net/wp-content/uploads/sites/125/2020/06/15182916/Sofitel-Dubai-Wafi-Luxury-Room-Bedroom-Skyline-View-Image1_WEB.jpg");
        long room9Id = roomService.createRoom(room9);

// Room 10
        Room room10 = new Room("111", "Superior", 180.00, 2,
                "Upgraded room with elegant decor", true, "WiFi, TV, Mini-bar",
                "https://aremorch.com/wp-content/uploads/2016/09/The-Details-That-Matter-Top-Things-Every-Luxury-Hotel-Room-Should-Have.png");
        long room10Id = roomService.createRoom(room10);

    }
}
