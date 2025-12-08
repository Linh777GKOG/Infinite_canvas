1. Tổng quan Dự án
Dự án nhằm phát triển một ứng dụng Android lấy cảm hứng từ ứng dụng Concepts – một công cụ vẽ và thiết kế chuyên nghiệp với canvas vô hạn. Ứng dụng sẽ cho phép người dùng vẽ, phác thảo ý tưởng, thiết kế đồ họa trên một không gian canvas không giới hạn kích thước, hỗ trợ zoom, pan mượt mà và các công cụ sáng tạo cơ bản.
Mục tiêu chính:
Cung cấp trải nghiệm vẽ tự do, phù hợp cho nghệ sĩ, designer, sinh viên hoặc người dùng sáng tạo.
Tập trung vào tính di động trên Android, với giao diện thân thiện, hiệu suất cao và tối ưu hóa cho thiết bị cảm ứng.
Đối tượng người dùng: Người dùng Android từ 18 tuổi trở lên, đặc biệt là những người yêu thích nghệ thuật kỹ thuật số, không cần phần cứng chuyên dụng như bút stylus (nhưng hỗ trợ nếu có).
Phạm vi: Phiên bản đầu tiên tập trung vào các tính năng cốt lõi, không bao gồm tích hợp đám mây hoặc chia sẻ xã hội. Dự án dự kiến hoàn thành trong 3-6 tháng tùy thuộc vào nguồn lực.
2. Chức năng
Ứng dụng sẽ bao gồm các chức năng chính sau, được chia thành nhóm để dễ quản lý:
Chức năng cốt lõi (Core Features):
Canvas vô hạn: Người dùng có thể vẽ, di chuyển, zoom in/out mà không bị giới hạn kích thước.
Công cụ vẽ cơ bản: Bút chì, bút mực, cọ vẽ, tẩy, với tùy chỉnh kích thước, màu sắc và độ mờ.
Hỗ trợ layer: Thêm/xóa layer, sắp xếp thứ tự, ẩn/hiện layer để quản lý nội dung phức tạp.
Chức năng tương tác (Interaction Features):
Gesture hỗ trợ: Pinch-to-zoom, two-finger pan, undo/redo nhanh chóng.
Chế độ chọn và chỉnh sửa: Chọn vùng vẽ để di chuyển, xoay, scale.
Hỗ trợ nhập văn bản: Thêm text box với font tùy chỉnh.
Chức năng phụ trợ (Utility Features):
Xuất file: Lưu dưới dạng PNG, SVG hoặc PDF.
Palette màu: Chọn màu từ bảng màu hoặc eyedropper tool.
Grid và ruler: Hỗ trợ lưới tham chiếu cho vẽ chính xác.
Chức năng nâng cao (Future Features – Phase 2):
Tích hợp brush tùy chỉnh, import/export từ các app khác.
Hỗ trợ stylus với pressure sensitivity (nếu thiết bị hỗ trợ).
3. Ngôn ngữ và Công cụ
Ngôn ngữ lập trình: Kotlin, có thể kết hợp Java cho một số module.
Công cụ phát triển:
IDE: Android Studio
UI Framework: Jetpack Compose.
Thư viện chính:
Canvas và Graphics: Android Canvas API cho vẽ, kết hợp với Path và Paint classes.
Gesture Detection: GestureDetector và ScaleGestureDetector cho xử lý chạm.
Layer Management: Sử dụng SurfaceView hoặc Custom View để render layer.
Xuất file: Android Bitmap và VectorDrawable cho PNG/SVG.
Các thư viện bên thứ ba: Glide (cho xử lý hình ảnh), Gson (cho lưu trữ dữ liệu), Room (nếu cần database локальный cho project history).
Công cụ test: Espresso cho UI test, JUnit cho unit test.
Quản lý mã nguồn: Git với GitHub hoặc GitLab.
Yêu cầu môi trường: Android SDK 21+ 
4. Giải pháp Kỹ thuật
Triển khai Infinite Canvas:
Sử dụng Custom View kế thừa từ View hoặc SurfaceView để render canvas. Để xử lý "vô hạn", không tạo bitmap khổng lồ mà sử dụng kỹ thuật tiling: Phân chia canvas thành các tile (ví dụ 1024x1024 pixels), chỉ load và render tile đang hiển thị trên màn hình. Khi zoom/pan, tính toán vị trí và load tile tương ứng để tiết kiệm bộ nhớ.
Xử lý zoom/pan: Sử dụng Matrix để transform canvas, kết hợp với GestureDetector để phát hiện cử chỉ. Giới hạn zoom từ 0.1x đến 10x để tránh lỗi hiệu suất.
Quản lý Layer và Vẽ:
Mỗi layer là một Bitmap riêng biệt, composite chúng khi render. Sử dụng PorterDuff modes cho blending (ví dụ: overlay, multiply).
Công cụ vẽ: Lưu đường vẽ dưới dạng Path objects, render realtime với anti-aliasing để mượt mà.
Tối ưu hóa Hiệu suất:
Off-screen rendering: Vẽ trên Bitmap cache trước khi apply lên màn hình.
Memory Management: Sử dụng WeakReference cho objects lớn, recycle Bitmap khi không dùng.
Threading: Sử dụng Handler hoặc Coroutines (Kotlin) để xử lý background tasks như lưu file mà không block UI.
Bảo mật và Lỗi:
Xử lý exception cho out-of-memory (OOM) bằng cách giới hạn số layer (tối đa 10).
Hỗ trợ đa thiết bị: Test trên emulator và real devices với kích thước màn hình khác nhau.
5. Kế hoạch
Kế hoạch phát triển được chia thành các giai đoạn theo mô hình Agile, với sprint 2 tuần/lần. Tổng thời gian ước tính: 4 tháng cho MVP (Minimum Viable Product).
Giai đoạn 1: Chuẩn bị 
Nghiên cứu và thiết kế: Phân tích Concepts app, vẽ wireframe UI/UX bằng Figma hoặc Adobe XD.
Thiết lập môi trường: Cài đặt Android Studio, tạo repository Git.
Đội ngũ: 1-2 developer (Android dev), 1 designer nếu có.
Giai đoạn 2: Phát triển Core
Xây dựng canvas cơ bản và gesture handling.
Triển khai công cụ vẽ và layer.
Test unit và integration.
Giai đoạn 3: Phát triển Features Phụ:
Thêm xuất file, palette màu, grid.
Tối ưu hóa hiệu suất và fix bug.
UI polish: Thêm animation mượt mà.
Giai đoạn 4: Testing và Deploy :
Test toàn diện: Manual test trên devices, automated test.
Beta release: Upload lên Google Play Console cho internal testing.
Fix feedback và release MVP.
Giai đoạn 5: Bảo trì và Mở rộng (Sau release):
Theo dõi user feedback qua Google Play.
Lập kế hoạch update: Thêm features nâng cao trong phase 2.
Lịch trình chi tiết: Sử dụng Trello hoặc Jira để track tasks. Milestone: Demo prototype cuối giai đoạn 2.


