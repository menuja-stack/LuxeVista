package com.example.luxevista.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxevista.R;
import com.example.luxevista.models.Booking;
import com.example.luxevista.service.BookingService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookings;
    private Context context;
    private BookingService bookingService;

    public BookingAdapter(Context context, List<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
        this.bookingService = new BookingService(context);
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        holder.tvRoomId.setText("Room: " + booking.getRoomId());
        holder.tvBookingDate.setText("Date: " + booking.getBookingDate());
        holder.tvDuration.setText("Duration: " + booking.getDuration() + " hours");
        holder.tvStatus.setText("Status: " + booking.getStatus());

        boolean isFutureBooking = isFutureDate(booking.getBookingDate());
        holder.btnEdit.setEnabled(isFutureBooking);
        holder.btnDelete.setEnabled(isFutureBooking);

        holder.btnEdit.setOnClickListener(v -> showEditDialog(booking));
        holder.btnDelete.setOnClickListener(v -> showDeleteDialog(booking));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    private boolean isFutureDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date bookingDate = sdf.parse(dateStr);
            Date currentDate = new Date();
            return bookingDate != null && bookingDate.after(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showEditDialog(Booking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_booking, null);

        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        EditText etDuration = dialogView.findViewById(R.id.etDuration);

        // Set current values
        etDuration.setText(String.valueOf(booking.getDuration()));

        builder.setView(dialogView)
                .setTitle("Edit Booking")
                .setPositiveButton("Save", (dialog, which) -> {
                    // Validate input
                    String durationStr = etDuration.getText().toString();
                    if (durationStr.isEmpty()) {
                        Toast.makeText(context, "Please enter duration", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int duration = Integer.parseInt(durationStr);
                    if (duration <= 0 || duration > 24) {
                        Toast.makeText(context, "Duration must be between 1 and 24 hours", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get selected date
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String newDate = sdf.format(calendar.getTime());

                    // Update booking
                    booking.setBookingDate(newDate);
                    booking.setDuration(duration);

                    bookingService.updateBooking(booking);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Booking updated successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteDialog(Booking booking) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Booking")
                .setMessage("Are you sure you want to delete this booking?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    bookingService.deleteBooking(booking.getId());
                    bookings.remove(booking);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Booking deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomId, tvBookingDate, tvDuration, tvStatus;
        Button btnEdit, btnDelete;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomId = itemView.findViewById(R.id.tvRoomId);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}