package com.pahelukadam.pahelukadam.ui.account

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pahelukadam.pahelukadam.MainActivity
import com.pahelukadam.pahelukadam.admin.Adminsigninpage
import com.pahelukadam.pahelukadam.databinding.FragmentAccountBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    // Counter for logo clicks
    private var logoClickCount = 0
    private val clickResetHandler = Handler(Looper.getMainLooper())

    // Camera and gallery request codes
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private val PERMISSION_REQUEST_CAMERA = 100
    private val PERMISSION_REQUEST_GALLERY = 101

    // Current photo path for camera
    private var currentPhotoPath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        setupClickListeners()
        return view
    }

    private fun setupClickListeners() {
        // ✅ Secret Feature: Triple Click Logo -> Adminsigninpage
        binding.logo.setOnClickListener {
            logoClickCount++
            clickResetHandler.removeCallbacksAndMessages(null)

            if (logoClickCount == 3) {
                logoClickCount = 0
                startActivity(Intent(requireContext(), Adminsigninpage::class.java))
            } else {
                clickResetHandler.postDelayed({ logoClickCount = 0 }, 1500)
            }
        }

        // Profile Image click listener
        binding.profileImage.setOnClickListener {
            showImagePickerDialog()
        }

        // Edit Profile button
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        // Add Mobile button
        binding.btnAddMobile.setOnClickListener {
            startActivity(Intent(requireContext(), AddMobileActivity::class.java))
        }

        // Sign Out button
        binding.btnSignOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
        loadProfileImage()
    }

    /**
     * Show dialog to choose camera or gallery
     */
    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Profile Picture")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> checkCameraPermission()
                1 -> checkGalleryPermission()
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    /**
     * Check camera permission
     */
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CAMERA
            )
        } else {
            openCamera()
        }
    }

    /**
     * Check gallery permission
     */
    private fun checkGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission), PERMISSION_REQUEST_GALLERY)
        } else {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(requireContext(), "Camera permission is required to take photos", Toast.LENGTH_LONG).show()
                }
            }
            PERMISSION_REQUEST_GALLERY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(requireContext(), "Storage permission is required to choose photos", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Open camera to take photo
     */
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                Log.e("AccountFragment", "Error creating image file", ex)
                Toast.makeText(requireContext(), "Error creating file for photo", Toast.LENGTH_SHORT).show()
                null
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.pahelukadam.pahelukadam.provider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        } else {
            Toast.makeText(requireContext(), "No camera app available", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Open gallery to choose photo
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    /**
     * Create temporary file for camera photo
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        if (storageDir != null && !storageDir.exists()) {
            storageDir.mkdirs()
        }

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    // Handle camera result
                    currentPhotoPath?.let { path ->
                        val bitmap = decodeSampledBitmapFromFile(path, 400, 400)
                        bitmap?.let {
                            saveProfileImage(it)
                            binding.profileImage.setImageBitmap(it)
                            Toast.makeText(requireContext(), "Profile picture updated from camera", Toast.LENGTH_SHORT).show()
                        } ?: run {
                            Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                REQUEST_IMAGE_PICK -> {
                    // Handle gallery result
                    data?.data?.let { uri ->
                        try {
                            val inputStream = requireContext().contentResolver.openInputStream(uri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            inputStream?.close()
                            bitmap?.let {
                                val resizedBitmap = resizeBitmap(it, 400, 400)
                                saveProfileImage(resizedBitmap)
                                binding.profileImage.setImageBitmap(resizedBitmap)
                                Toast.makeText(requireContext(), "Profile picture updated from gallery", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("AccountFragment", "Error loading image from gallery", e)
                            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // User cancelled the operation
            Log.d("AccountFragment", "User cancelled image selection")
        }
    }

    /**
     * Decode sampled bitmap from file to avoid memory issues
     */
    private fun decodeSampledBitmapFromFile(path: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        return try {
            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            BitmapFactory.decodeFile(path, options)
        } catch (e: Exception) {
            Log.e("AccountFragment", "Error decoding bitmap from file", e)
            null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * Resize bitmap to required dimensions
     */
    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        if (width > maxWidth || height > maxHeight) {
            val ratio = width.toFloat() / height.toFloat()
            if (ratio > 1) {
                width = maxWidth
                height = (maxWidth / ratio).toInt()
            } else {
                height = maxHeight
                width = (maxHeight * ratio).toInt()
            }
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    /**
     * Save profile image to app's internal storage
     */
    private fun saveProfileImage(bitmap: Bitmap) {
        try {
            val file = File(requireContext().filesDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.flush()
            outputStream.close()
            Log.d("AccountFragment", "Profile image saved successfully")
        } catch (e: Exception) {
            Log.e("AccountFragment", "Error saving profile image", e)
            Toast.makeText(requireContext(), "Error saving profile picture", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Load profile image from internal storage
     */
    private fun loadProfileImage() {
        try {
            val file = File(requireContext().filesDir, "profile_image.jpg")
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                bitmap?.let {
                    binding.profileImage.setImageBitmap(it)
                }
                Log.d("AccountFragment", "Profile image loaded successfully")
            } else {
                Log.d("AccountFragment", "No saved profile image found")
            }
        } catch (e: Exception) {
            Log.e("AccountFragment", "Error loading profile image", e)
        }
    }

    /**
     * 🔄 Animate logo loader with smooth fade in/out loop
     */
    private fun startLogoAnimation() {
        val logo = binding.logoLoader
        logo.animate()
            .alpha(0f)
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                logo.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(600)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        if (logo.visibility == View.VISIBLE) startLogoAnimation()
                    }
                    .start()
            }
            .start()
    }

    /**
     * ✨ Smoothly show content after data load
     */
    private fun showContent() {
        binding.logoLoader.clearAnimation()
        binding.logoLoader.visibility = View.GONE
        binding.contentLayout.alpha = 0f
        binding.contentLayout.visibility = View.VISIBLE
        binding.contentLayout.animate()
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun loadUserData() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            // 🔄 Show loader animation, hide content
            binding.logoLoader.visibility = View.VISIBLE
            binding.contentLayout.visibility = View.GONE
            startLogoAnimation()

            val db = Firebase.firestore
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        val email = document.getString("email") ?: ""

                        binding.tvUserName.text = "$firstName $lastName"
                        binding.tvUserEmail.text = email
                    } else {
                        Log.d("AccountFragment", "No user profile document found")
                    }
                    showContent() // ✅ smooth transition after loading
                }
                .addOnFailureListener { exception ->
                    Log.e("AccountFragment", "Error fetching user data", exception)
                    Toast.makeText(requireContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show()
                    showContent() // show content even on failure
                }
        } else {
            // If no user is logged in
            binding.logoLoader.visibility = View.GONE
            binding.contentLayout.visibility = View.VISIBLE
            binding.tvUserName.text = "Guest User"
            binding.tvUserEmail.text = "Not signed in"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clickResetHandler.removeCallbacksAndMessages(null)
        _binding = null
    }
}