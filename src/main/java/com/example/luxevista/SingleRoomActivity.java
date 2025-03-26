package com.example.luxevista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.example.luxevista.models.Booking;
import com.example.luxevista.models.Room;
import com.example.luxevista.service.BookingService;
import com.example.luxevista.service.PreferencesManager;
import com.example.luxevista.service.RoomService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;



public class SingleRoomActivity extends AppCompatActivity {
    private RoomService roomService;

    private PreferencesManager preferencesManager;
    private Room room;
    private ImageView imageViewPager;
    private TabLayout imageIndicator;
    private TextView roomTitle;
    private RatingBar ratingBar;
    private TextView ratingText;
    private ChipGroup amenitiesChipGroup;
    private TextView descriptionText;
    private TextView priceText;
    private TextView nightsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_room);

        roomService = new RoomService(this);
        initializeViews();
        preferencesManager = new PreferencesManager(this);

        int roomId = getIntent().getIntExtra("ROOM_ID", -1);
        if (roomId != -1) {
            loadRoom(roomId);
        } else {
            finish();
        }
    }

    private void initializeViews() {
        imageViewPager = findViewById(R.id.roomImageView);
        imageIndicator = findViewById(R.id.imageIndicator);
        roomTitle = findViewById(R.id.roomTitle);
        ratingBar = findViewById(R.id.ratingBar);
        ratingText = findViewById(R.id.ratingText);
        amenitiesChipGroup = findViewById(R.id.amenitiesChipGroup);
        descriptionText = findViewById(R.id.descriptionText);
        priceText = findViewById(R.id.priceText);
        nightsText = findViewById(R.id.nightsText);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        findViewById(R.id.shareButton).setOnClickListener(v -> shareRoom());
        findViewById(R.id.bookNowButton).setOnClickListener(v -> bookRoom());
    }

    private void loadRoom(int roomId) {
        room = roomService.getRoom(roomId);
        if (room != null) {
            updateUI();
        }
    }

    private void updateUI() {
        roomTitle.setText("Entire " + room.getRoomType());
        ratingBar.setRating(4.92f); // Example rating
        ratingText.setText("4.92 (116 reviews)"); // Example reviews
        descriptionText.setText(room.getDescription());
        priceText.setText(String.format(Locale.getDefault(), "€%.2f", room.getPrice()));
        nightsText.setText("3 nights"); // Example nights

        // Setup amenities
        String[] amenities = room.getAmenities().split(",");
        for (String amenity : amenities) {
            Chip chip = new Chip(this);
            chip.setText(amenity.trim());
            amenitiesChipGroup.addView(chip);
        }

        // Setup image viewpager (you'll need to create an ImageAdapter)
        // For now, just loading the single image
        if (room.getImageUrl() != null) {
            ImageView roomImageView = findViewById(R.id.roomImageView);

// Load the image URL
            String imageUrl = room.getImageUrl(); // Replace with your Room object image URL

            if (imageUrl != null) {
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_room) // Optional placeholder
                        .error(R.drawable.error_room)           // Optional error image
                        .into(roomImageView);
            }

        }
    }

    private void shareRoom() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Check out this amazing room: " + room.getRoomType() +
                        " at " + room.getPrice() + " per night");
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void bookRoom() {
        // Create an AlertDialog with custom layout for booking details
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_booking_details, null);
        builder.setTitle("Book Room");

        // Find views in the dialog layout
        EditText editTextBookingDate = dialogView.findViewById(R.id.editTextBookingDate);
        EditText editTextDuration = dialogView.findViewById(R.id.editTextDuration);

        // Set up date picker for booking date
        editTextBookingDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        // Format the selected date
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        editTextBookingDate.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            // Set minimum date to today
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        builder.setView(dialogView);

        builder.setPositiveButton("Book", (dialog, which) -> {
            // Validate booking details
            String bookingDateStr = editTextBookingDate.getText().toString().trim();
            String durationStr = editTextDuration.getText().toString().trim();

            // Validation checks
            if (validateBookingDetails(bookingDateStr, durationStr)) {
                // Parse duration
                int duration = Integer.parseInt(durationStr);

                // Retrieve the user ID from preferences
                int userId = preferencesManager.getUserId();
                int roomId = room.getId();
                String status = "Pending";

                // Create a Booking object
                Booking booking = new Booking(userId, roomId, bookingDateStr, duration, status);

                // Save the booking using the BookingService
                BookingService bookingService = new BookingService(this);
                long bookingId = bookingService.createBooking(booking);

                // Provide feedback to the user
                if (bookingId > 0) {
                    Toast.makeText(this, "Room booked successfully with Booking ID: " + bookingId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to book the room. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validateBookingDetails(String bookingDateStr, String durationStr) {
        // Check if booking date is empty
        if (TextUtils.isEmpty(bookingDateStr)) {
            Toast.makeText(this, "Please select a booking date", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if duration is empty
        if (TextUtils.isEmpty(durationStr)) {
            Toast.makeText(this, "Please enter the number of nights", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate duration
        try {
            int duration = Integer.parseInt(durationStr);
            if (duration < 1) {
                Toast.makeText(this, "Duration must be at least 1 night", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (duration > 30) {
                Toast.makeText(this, "Duration cannot exceed 30 nights", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid duration. Please enter a valid number", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate booking date format
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setLenient(false);
            Date bookingDate = sdf.parse(bookingDateStr);

            // Ensure booking date is in the future
            if (bookingDate == null || bookingDate.before(new Date())) {
                Toast.makeText(this, "Booking date must be in the future", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Use YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}