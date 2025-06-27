-- Thêm dữ liệu vào bảng roles
INSERT INTO roles (role_id, role_name, description, created_at, updated_at) VALUES
                                                                                (1, 'ADMIN', 'Quản trị viên hệ thống', '2025-06-27 09:00:00', '2025-06-27 09:00:00'),
                                                                                (2, 'USER', 'Người dùng thông thường', '2025-06-27 09:00:00', '2025-06-27 09:00:00'),
                                                                                (3, 'MODERATOR', 'Điều hành viên nội dung', '2025-06-27 09:00:00', '2025-06-27 09:00:00');

-- Thêm dữ liệu vào bảng permissions
INSERT INTO permissions (permission_id, role_id, permission_name, description, is_allowed, created_at, updated_at) VALUES
                                                                                                                       (1, 1, 'MANAGE_USERS', 'Quản lý người dùng', TRUE, '2025-06-27 09:01:00', '2025-06-27 09:01:00'),
                                                                                                                       (2, 1, 'MANAGE_CONTENT', 'Quản lý nội dung', TRUE, '2025-06-27 09:01:00', '2025-06-27 09:01:00'),
                                                                                                                       (3, 2, 'POST_CONTENT', 'Đăng bài viết và video', TRUE, '2025-06-27 09:01:00', '2025-06-27 09:01:00'),
                                                                                                                       (4, 3, 'MODERATE_CONTENT', 'Kiểm duyệt nội dung', TRUE, '2025-06-27 09:01:00', '2025-06-27 09:01:00');

-- Thêm dữ liệu vào bảng users
INSERT INTO users (user_id, user_name, phone_number, email, password, role_id, status, created_at, updated_at, last_login, address, bio, date_of_birth, profile_picture, cover_photo, user_code, two_fa_secret, copyright_violations, warning_count, qr_code) VALUES
                                                                                                                                                                                                                                                                  (1, 'admin', '0901234567', 'admin@example.com', 'hashed_password_admin', 1, 'ACTIVE', '2025-06-27 09:02:00', '2025-06-27 09:02:00', '2025-06-27 09:02:00', 'Hanoi, Vietnam', 'Quản trị viên hệ thống', '1980-01-01 00:00:00', 'https://example.com/admin_profile.jpg', 'https://example.com/admin_cover.jpg', 'ADMIN001', NULL, 0, 0, NULL),
                                                                                                                                                                                                                                                                  (2, 'user1', '0901234568', 'user1@example.com', 'hashed_password_user1', 2, 'ACTIVE', '2025-06-27 09:03:00', '2025-06-27 09:03:00', NULL, 'HCMC, Vietnam', 'Người dùng yêu thích âm nhạc', '1990-05-15 00:00:00', 'https://example.com/user1_profile.jpg', NULL, 'USER001', NULL, 0, 0, NULL),
                                                                                                                                                                                                                                                                  (3, 'user2', '0901234569', 'user2@example.com', 'hashed_password_user2', 2, 'ACTIVE', '2025-06-27 09:04:00', '2025-06-27 09:04:00', NULL, 'Da Nang, Vietnam', 'Người dùng yêu thích game', '1995-10-20 00:00:00', 'https://example.com/user2_profile.jpg', NULL, 'USER002', NULL, 1, 1, NULL);

-- Thêm dữ liệu vào bảng video_categories
INSERT INTO video_categories (category_id, category_name, icon_url, description) VALUES
                                                                                     (1, 'Music', 'https://example.com/music_icon.png', 'Videos âm nhạc'),
                                                                                     (2, 'Gaming', 'https://example.com/gaming_icon.png', 'Videos về game'),
                                                                                     (3, 'Education', 'https://example.com/education_icon.png', 'Videos giáo dục'),
                                                                                     (4, 'Vlog', 'https://example.com/vlog_icon.png', 'Videos vlog cá nhân'),
                                                                                     (5, 'Pop', 'https://example.com/pop_icon.png', 'Videos nhạc Pop'),
                                                                                     (6, 'Rock', 'https://example.com/rock_icon.png', 'Videos nhạc Rock'),
                                                                                     (7, 'Tutorial', 'https://example.com/tutorial_icon.png', 'Videos hướng dẫn');

-- Thêm dữ liệu vào bảng follows
INSERT INTO follows (follower_id, followed_id, created_at) VALUES
                                                               (2, 3, '2025-06-27 09:05:00'),
                                                               (3, 2, '2025-06-27 09:06:00');

-- Thêm dữ liệu vào bảng posts
INSERT INTO posts (post_id, user_id, content, visibility, created_at, updated_at) VALUES
                                                                                      (1, 2, 'Chào mọi người, đây là bài đăng đầu tiên của tôi!', 'PUBLIC', '2025-06-27 09:07:00', '2025-06-27 09:07:00'),
                                                                                      (2, 3, 'Hôm nay là một ngày tuyệt vời!', 'PUBLIC', '2025-06-27 09:08:00', '2025-06-27 09:08:00'),
                                                                                      (3, 2, 'Cảm ơn mọi người đã ủng hộ!', 'ONLY_ME', '2025-06-27 09:09:00', '2025-06-27 09:09:00');

-- Thêm dữ liệu vào bảng post_media
INSERT INTO post_media (media_id, post_id, file_url, media_type, created_at) VALUES
                                                                                 (1, 1, 'https://example.com/post1_image.jpg', 'IMAGE', '2025-06-27 09:07:00'),
                                                                                 (2, 2, 'https://example.com/post2_video.mp4', 'VIDEO', '2025-06-27 09:08:00');

-- Thêm dữ liệu vào bảng post_views
INSERT INTO post_views (view_id, post_id, user_id, viewed_at) VALUES
                                                                  (1, 1, 3, '2025-06-27 09:10:00'),
                                                                  (2, 2, 2, '2025-06-27 09:11:00');

-- Thêm dữ liệu vào bảng post_likes
INSERT INTO post_likes (post_id, user_id, created_at) VALUES
                                                          (1, 3, '2025-06-27 09:12:00'),
                                                          (2, 2, '2025-06-27 09:13:00');

-- Thêm dữ liệu vào bảng post_shares
INSERT INTO post_shares (share_id, post_id, user_id, share_content, created_at) VALUES
                                                                                    (1, 1, 3, 'Bài đăng này rất thú vị!', '2025-06-27 09:14:00'),
                                                                                    (2, 2, 2, 'Chia sẻ bài đăng này nhé mọi người!', '2025-06-27 09:15:00');

-- Thêm dữ liệu vào bảng videos
INSERT INTO videos (user_id, video_category_id, title, description, thumbnail_url, visibility, total_duration, created_at, updated_at) VALUES
                                                                                                                                           (2, 5, 'MV Nhạc Pop Mới Nhất 2025', 'Video âm nhạc Pop mới nhất của tôi, phát hành ngày 27/06/2025!', 'https://example.com/thumbnail_pop2025.jpg', 'PUBLIC', 180, '2025-06-27 09:33:00', '2025-06-27 09:33:00'),
                                                                                                                                           (3, 7, 'Hướng Dẫn Chơi Game Mới', 'Hướng dẫn chi tiết cách chơi game hot nhất 2025', 'https://example.com/thumbnail_game2025.jpg', 'PUBLIC', 300, '2025-06-27 09:33:00', '2025-06-27 09:33:00'),
                                                                                                                                           (2, 6, 'MV Nhạc Rock Cảm Xúc', 'Video nhạc Rock đầy cảm xúc, ra mắt hôm nay!', 'https://example.com/thumbnail_rock2025.jpg', 'PUBLIC', 240, '2025-06-27 09:33:00', '2025-06-27 09:33:00');


-- Thêm dữ liệu vào bảng video_media
INSERT INTO video_media (media_id, video_id, file_url, created_at) VALUES
                                                                       (1, 1, 'https://example.com/video1.mp4', '2025-06-27 09:16:00'),
                                                                       (2, 2, 'https://example.com/video2.mp4', '2025-06-27 09:17:00');

-- Thêm dữ liệu vào bảng video_views
INSERT INTO video_views (view_id, video_id, user_id, viewed_at) VALUES
                                                                    (1, 1, 3, '2025-06-27 09:18:00'),
                                                                    (2, 2, 2, '2025-06-27 09:19:00');

-- Thêm dữ liệu vào bảng video_likes
INSERT INTO video_likes (video_id, user_id, created_at) VALUES
                                                            (1, 3, '2025-06-27 09:20:00'),
                                                            (2, 2, '2025-06-27 09:21:00');

-- Thêm dữ liệu vào bảng video_shares
INSERT INTO video_shares (share_id, video_id, user_id, share_content, created_at) VALUES
                                                                                      (1, 1, 3, 'Video này hay quá, mọi người xem nhé!', '2025-06-27 09:22:00'),
                                                                                      (2, 2, 2, 'Hướng dẫn chơi game rất chi tiết!', '2025-06-27 09:23:00');

-- Thêm dữ liệu vào bảng comments
INSERT INTO comments (comment_id, post_id, video_id, user_id, parent_comment_id, content, created_at, updated_at) VALUES
                                                                                                                      (1, 1, NULL, 3, NULL, 'Bài đăng tuyệt vời!', '2025-06-27 09:24:00', '2025-06-27 09:24:00'),
                                                                                                                      (2, NULL, 1, 2, NULL, 'Video này hay quá!', '2025-06-27 09:25:00', '2025-06-27 09:25:00'),
                                                                                                                      (3, 1, NULL, 2, 1, 'Cảm ơn bạn!', '2025-06-27 09:26:00', '2025-06-27 09:26:00');

-- Thêm dữ liệu vào bảng comment_images
INSERT INTO comment_images (image_id, comment_id, image_url, created_at) VALUES
                                                                             (1, 1, 'https://example.com/comment_image1.jpg', '2025-06-27 09:24:00'),
                                                                             (2, 2, 'https://example.com/comment_image2.jpg', '2025-06-27 09:25:00');

-- Thêm dữ liệu vào bảng comment_likes
INSERT INTO comment_likes (comment_id, user_id, created_at) VALUES
                                                                (1, 2, '2025-06-27 09:27:00'),
                                                                (2, 3, '2025-06-27 09:28:00');

-- Thêm dữ liệu vào bảng comment_mentions
INSERT INTO comment_mentions (comment_id, user_id) VALUES
                                                       (1, 2),
                                                       (2, 3);

-- Thêm dữ liệu vào bảng playlists
INSERT INTO playlists (playlist_id, user_id, playlist_name, description, thumbnail_url, visibility, created_at, updated_at) VALUES
                                                                                                                                (1, 2, 'Danh sách nhạc yêu thích', 'Những video âm nhạc hay nhất', 'https://example.com/playlist1_thumbnail.jpg', 'PUBLIC', '2025-06-27 09:29:00', '2025-06-27 09:29:00'),
                                                                                                                                (2, 3, 'Hướng dẫn game', 'Các video hướng dẫn chơi game', 'https://example.com/playlist2_thumbnail.jpg', 'PRIVATE', '2025-06-27 09:30:00', '2025-06-27 09:30:00');

-- Thêm dữ liệu vào bảng playlist_videos
INSERT INTO playlist_videos (playlist_id, video_id, added_at) VALUES
                                                                  (1, 1, '2025-06-27 09:31:00'),
                                                                  (2, 2, '2025-06-27 09:32:00');

-- Thêm dữ liệu vào bảng watch_later
INSERT INTO watch_later (user_id, video_id, added_at) VALUES
                                                          (2, 2, '2025-06-27 09:33:00'),
                                                          (3, 1, '2025-06-27 09:34:00');

-- Thêm dữ liệu vào bảng video_view_history
INSERT INTO video_view_history (user_id, video_id, viewed_at, duration_watched) VALUES
                                                                                    (3, 1, '2025-06-27 09:18:00', 120),
                                                                                    (2, 2, '2025-06-27 09:19:00', 180);

-- Thêm dữ liệu vào bảng report_reasons
INSERT INTO report_reasons (reason_id, reason_name, description, created_at) VALUES
                                                                                 (1, 'Nội dung không phù hợp', 'Nội dung vi phạm tiêu chuẩn cộng đồng', '2025-06-27 09:00:00'),
                                                                                 (2, 'Vi phạm bản quyền', 'Nội dung vi phạm bản quyền', '2025-06-27 09:00:00'),
                                                                                 (3, 'Lừa đảo', 'Nội dung có dấu hiệu lừa đảo', '2025-06-27 09:00:00');

-- Thêm dữ liệu vào bảng reports
INSERT INTO reports (report_id, reporter_id, post_id, video_id, comment_id, user_id, reason_id, description, status, admin_id, admin_note, created_at, updated_at) VALUES
                                                                                                                                                                       (1, 2, 1, NULL, NULL, NULL, 1, 'Bài đăng có nội dung không phù hợp', 'PENDING', NULL, NULL, '2025-06-27 09:35:00', '2025-06-27 09:35:00'),
                                                                                                                                                                       (2, 3, NULL, 1, NULL, NULL, 2, 'Video vi phạm bản quyền', 'PENDING', NULL, NULL, '2025-06-27 09:36:00', '2025-06-27 09:36:00');

-- Thêm dữ liệu vào bảng notifications
INSERT INTO notifications (notification_id, receiver_id, actor_id, type, post_id, video_id, comment_id, report_id, playlist_id, content, is_read, created_at) VALUES
                                                                                                                                                                  (1, 2, 3, 'LIKE_POST', 1, NULL, NULL, NULL, NULL, 'User2 đã thích bài đăng của bạn', FALSE, '2025-06-27 09:12:00'),
                                                                                                                                                                  (2, 3, 2, 'LIKE_VIDEO', NULL, 1, NULL, NULL, NULL, 'User1 đã thích video của bạn', FALSE, '2025-06-27 09:20:00'),
                                                                                                                                                                  (3, 2, 3, 'NEW_COMMENT_POST', 1, NULL, 1, NULL, NULL, 'User2 đã bình luận bài đăng của bạn', FALSE, '2025-06-27 09:24:00');

-- Thêm dữ liệu vào bảng wallets
INSERT INTO wallets (wallet_id, user_id, balance, created_at, updated_at) VALUES
                                                                              (1, 2, 1000.00, '2025-06-27 09:00:00', '2025-06-27 09:00:00'),
                                                                              (2, 3, 500.00, '2025-06-27 09:00:00', '2025-06-27 09:00:00');

-- Thêm dữ liệu vào bảng wallet_transactions
INSERT INTO wallet_transactions (transaction_id, wallet_id, type, amount, status, description, created_at) VALUES
                                                                                                               (1, 1, 'DEPOSIT', 1000.00, 'SUCCESS', 'Nạp tiền vào ví', '2025-06-27 09:01:00'),
                                                                                                               (2, 2, 'DEPOSIT', 500.00, 'SUCCESS', 'Nạp tiền vào ví', '2025-06-27 09:02:00');

-- Thêm dữ liệu vào bảng video_copyrights
INSERT INTO video_copyrights (copyright_id, video_id, owner_id, is_ai_generated, status, claim_description, claimant_id, report_id, created_at, updated_at) VALUES
                                                                                                                                                                (1, 1, 2, FALSE, 'VERIFIED', NULL, NULL, NULL, '2025-06-27 09:16:00', '2025-06-27 09:16:00'),
                                                                                                                                                                (2, 2, 3, FALSE, 'DISPUTED', 'Video bị báo cáo vi phạm bản quyền', 2, 2, '2025-06-27 09:36:00', '2025-06-27 09:36:00');

-- Thêm dữ liệu vào bảng copyright_violations
INSERT INTO copyright_violations (violation_id, user_id, video_id, violation_count, penalty_applied, fine_amount, created_at) VALUES
    (1, 3, 2, 1, 'WARNING', 0.00, '2025-06-27 09:37:00');

-- Thêm dữ liệu vào bảng ad_types
INSERT INTO ad_types (ad_type_id, ad_type_name, description, created_at) VALUES
                                                                             (1, 'Banner', 'Quảng cáo dạng biểu ngữ', '2025-06-27 09:00:00'),
                                                                             (2, 'Video Ad', 'Quảng cáo dạng video', '2025-06-27 09:00:00');

-- Thêm dữ liệu vào bảng ad_campaigns
INSERT INTO ad_campaigns (campaign_id, user_id, wallet_id, campaign_name, objective, budget, spent_amount, start_date, end_date, status, target_audience, created_at, updated_at) VALUES
    (1, 2, 1, 'Chiến dịch quảng cáo nhạc', 'VIDEO_VIEWS', 500.00, 100.00, '2025-06-27 09:38:00', '2025-07-27 09:38:00', 'ACTIVE', 'Người yêu âm nhạc, 18-35 tuổi', '2025-06-27 09:38:00', '2025-06-27 09:38:00');

-- Thêm dữ liệu vào bảng ads
INSERT INTO ads (ad_id, campaign_id, ad_type_id, video_id, post_id, content, status, impressions, clicks, created_at, updated_at) VALUES
    (1, 1, 2, 1, NULL, 'Quảng cáo video âm nhạc mới', 'APPROVED', 1000, 50, '2025-06-27 09:39:00', '2025-06-27 09:39:00');

-- Thêm dữ liệu vào bảng video_monetization
INSERT INTO video_monetization (monetization_id, video_id, user_id, status, total_revenue, created_at, updated_at) VALUES
                                                                                                                       (1, 1, 2, 'APPROVED', 50.00, '2025-06-27 09:40:00', '2025-06-27 09:40:00'),
                                                                                                                       (2, 2, 3, 'PENDING', 0.00, '2025-06-27 09:41:00', '2025-06-27 09:41:00');

-- Thêm dữ liệu vào bảng ad_revenue
INSERT INTO ad_revenue (revenue_id, monetization_id, ad_id, revenue_amount, revenue_type, impressions, clicks, created_at) VALUES
    (1, 1, 1, 50.00, 'CPM', 1000, 50, '2025-06-27 09:42:00');

-- Thêm dữ liệu vào bảng video_analytics
INSERT INTO video_analytics (analytics_id, video_id, user_id, period_type, period_value, view_count, like_count, revenue_amount, created_at, updated_at) VALUES
                                                                                                                                                             (1, 1, 2, 'DAY', '2025-06-27', 100, 10, 50.00, '2025-06-27 09:43:00', '2025-06-27 09:43:00'),
                                                                                                                                                             (2, 2, 3, 'DAY', '2025-06-27', 50, 5, 0.00, '2025-06-27 09:44:00', '2025-06-27 09:44:00');