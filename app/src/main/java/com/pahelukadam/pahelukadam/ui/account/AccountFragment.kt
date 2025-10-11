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

    private var logoClickCount = 0
    private val clickResetHandler = Handler(Looper.getMainLooper())

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private val PERMISSION_REQUEST_CAMERA = 100
    private val PERMISSION_REQUEST_GALLERY = 101

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
        // Secret triple-click to open Admin Sign-in
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

        // Profile image click
        binding.profileImage.setOnClickListener {
            showImagePickerDialog()
        }

        // Edit Profile
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        // Add Mobile
        binding.btnAddMobile.setOnClickListener {
            startActivity(Intent(requireContext(), AddMobileActivity::class.java))
        }

        // Sign Out
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

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
        } else {
            openCamera()
        }
    }

    private fun checkGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(permission), PERMISSION_REQUEST_GALLERY)
        } else {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) openCamera()
                else Toast.makeText(requireContext(), "Camera permission required", Toast.LENGTH_SHORT).show()
            }
            PERMISSION_REQUEST_GALLERY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) openGallery()
                else Toast.makeText(requireContext(), "Gallery permission required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                Toast.makeText(requireContext(), "Error creating image file", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir != null && !storageDir.exists()) storageDir.mkdirs()
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    currentPhotoPath?.let { path ->
                        val bitmap = decodeSampledBitmapFromFile(path, 400, 400)
                        bitmap?.let {
                            saveProfileImage(it)
                            binding.profileImage.setImageBitmap(it)
                            Toast.makeText(requireContext(), "Profile picture updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                REQUEST_IMAGE_PICK -> {
                    data?.data?.let { uri ->
                        try {
                            val inputStream = requireContext().contentResolver.openInputStream(uri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            inputStream?.close()
                            bitmap?.let {
                                val resized = resizeBitmap(it, 400, 400)
                                saveProfileImage(resized)
                                binding.profileImage.setImageBitmap(resized)
                                Toast.makeText(requireContext(), "Profile picture updated", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun decodeSampledBitmapFromFile(path: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height, width) = options.run { outHeight to outWidth }
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

    private fun saveProfileImage(bitmap: Bitmap) {
        try {
            val file = File(requireContext().filesDir, "profile_image.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error saving image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfileImage() {
        try {
            val file = File(requireContext().filesDir, "profile_image.jpg")
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                binding.profileImage.setImageBitmap(bitmap)
                Log.d("AccountFragment", "Profile image loaded")
            } else {
                // ✅ Show default icon if no saved image
                binding.profileImage.setImageResource(com.pahelukadam.pahelukadam.R.drawable.ic_account_24)
                Log.d("AccountFragment", "No saved image found, showing default")
            }
        } catch (e: Exception) {
            binding.profileImage.setImageResource(com.pahelukadam.pahelukadam.R.drawable.ic_account_24)
            Log.e("AccountFragment", "Error loading profile image", e)
        }
    }

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
            }.start()
    }

    private fun showContent() {
        binding.logoLoader.clearAnimation()
        binding.logoLoader.visibility = View.GONE
        binding.contentLayout.alpha = 0f
        binding.contentLayout.visibility = View.VISIBLE
        binding.contentLayout.animate().alpha(1f).setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator()).start()
    }

    private fun loadUserData() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
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
                    }
                    showContent()
                }
                .addOnFailureListener {
                    showContent()
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
        } else {
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
