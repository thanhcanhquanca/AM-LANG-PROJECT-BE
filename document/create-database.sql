-- Xóa cơ sở dữ liệu goyi_projects nếu đã tồn tại
DROP DATABASE IF EXISTS amlang_projects;

-- Tạo cơ sở dữ liệu goyi_projects
CREATE DATABASE amlang_projects;
USE amlang_projects;

select * from videos;

-- Tạo bảng roles
CREATE TABLE roles (
                       role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       role_name VARCHAR(50) NOT NULL UNIQUE,
                       description TEXT,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL
);

-- Tạo bảng permissions
CREATE TABLE permissions (
                             permission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             role_id BIGINT NOT NULL,
                             permission_name VARCHAR(100) NOT NULL,
                             description TEXT,
                             is_allowed BOOLEAN NOT NULL,
                             created_at TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP NOT NULL,
                             FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE RESTRICT
);

-- Tạo bảng users
CREATE TABLE users (
                       user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       user_name VARCHAR(50) NOT NULL,
                       phone_number VARCHAR(20) NOT NULL UNIQUE,
                       email VARCHAR(255) UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role_id BIGINT NOT NULL,
                       status ENUM('ACTIVE', 'INACTIVE', 'LOCKED', 'SUSPENDED', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,
                       last_login TIMESTAMP,
                       address TEXT,
                       bio TEXT,
                       date_of_birth TIMESTAMP,
                       profile_picture VARCHAR(255),
                       cover_photo VARCHAR(255),
                       user_code VARCHAR(50) UNIQUE,
                       two_fa_secret VARCHAR(255),
                       copyright_violations INT DEFAULT 0,
                       warning_count INT DEFAULT 0,
                       qr_code VARCHAR(500),
                       FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE RESTRICT
);

-- Tạo bảng video_categories
CREATE TABLE video_categories (
                                  category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  category_name VARCHAR(100) NOT NULL UNIQUE,
                                  icon_url VARCHAR(255),
                                  description TEXT
);

-- Tạo bảng follows
CREATE TABLE follows (
                         follower_id BIGINT NOT NULL,
                         followed_id BIGINT NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (follower_id, followed_id),
                         FOREIGN KEY (follower_id) REFERENCES users(user_id) ON DELETE CASCADE,
                         FOREIGN KEY (followed_id) REFERENCES users(user_id) ON DELETE CASCADE,
                         INDEX idx_followed_id (followed_id)
);

-- Tạo bảng posts
CREATE TABLE posts (
                       post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       user_id BIGINT NOT NULL,
                       content TEXT,
                       visibility ENUM('PUBLIC', 'PRIVATE', 'ONLY_ME') DEFAULT 'PUBLIC',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                       INDEX idx_user_id (user_id),
                       INDEX idx_visibility (visibility)
);

-- Tạo bảng post_media
CREATE TABLE post_media (
                            media_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            post_id BIGINT NOT NULL,
                            file_url VARCHAR(255) NOT NULL,
                            media_type ENUM('IMAGE', 'VIDEO') DEFAULT 'IMAGE',
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
                            INDEX idx_post_id (post_id)
);

-- Tạo bảng post_views
CREATE TABLE post_views (
                            view_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            post_id BIGINT NOT NULL,
                            user_id BIGINT,
                            viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
                            FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                            INDEX idx_post_id_user_id (post_id, user_id)
);

-- Tạo bảng post_likes
CREATE TABLE post_likes (
                            post_id BIGINT NOT NULL,
                            user_id BIGINT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (post_id, user_id),
                            FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
                            FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                            INDEX idx_user_id (user_id)
);

-- Tạo bảng post_shares
CREATE TABLE post_shares (
                             share_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             post_id BIGINT NOT NULL,
                             user_id BIGINT NOT NULL,
                             share_content TEXT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
                             FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                             INDEX idx_post_id (post_id),
                             INDEX idx_user_id (user_id)
);

-- Tạo bảng videos
CREATE TABLE videos (
                        video_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        video_category_id BIGINT NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        thumbnail_url VARCHAR(255) NOT NULL,
                        visibility ENUM('PUBLIC', 'PRIVATE', 'ONLY_ME') DEFAULT 'PUBLIC',
                        total_duration INT NOT NULL DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                        FOREIGN KEY (video_category_id) REFERENCES video_categories(category_id) ON DELETE RESTRICT,
                        INDEX idx_user_id (user_id),
                        INDEX idx_video_category_id (video_category_id),
                        INDEX idx_visibility (visibility)
);

-- Tạo bảng video_media
CREATE TABLE video_media (
                             media_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             video_id BIGINT NOT NULL,
                             file_url VARCHAR(255) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                             INDEX idx_video_id (video_id)
);

-- Tạo bảng video_views
CREATE TABLE video_views (
                             view_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             video_id BIGINT NOT NULL,
                             user_id BIGINT,
                             viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                             FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
                             INDEX idx_video_id_user_id (video_id, user_id)
);

-- Tạo bảng video_likes
CREATE TABLE video_likes (
                             video_id BIGINT NOT NULL,
                             user_id BIGINT NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (video_id, user_id),
                             FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                             FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                             INDEX idx_user_id (user_id)
);

-- Tạo bảng video_shares
CREATE TABLE video_shares (
                              share_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              video_id BIGINT NOT NULL,
                              user_id BIGINT NOT NULL,
                              share_content TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                              FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                              INDEX idx_video_id (video_id),
                              INDEX idx_user_id (user_id)
);

-- Tạo bảng comments
CREATE TABLE comments (
                          comment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          post_id BIGINT,
                          video_id BIGINT,
                          user_id BIGINT NOT NULL,
                          parent_comment_id BIGINT,
                          content TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
                          FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                          FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                          FOREIGN KEY (parent_comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE,
                          INDEX idx_post_id (post_id),
                          INDEX idx_video_id (video_id),
                          INDEX idx_user_id (user_id),
                          INDEX idx_parent_comment_id (parent_comment_id),
                          CHECK (post_id IS NOT NULL XOR video_id IS NOT NULL)
);

-- Tạo bảng comment_images
CREATE TABLE comment_images (
                                image_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                comment_id BIGINT NOT NULL,
                                image_url VARCHAR(255) NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE,
                                INDEX idx_comment_id (comment_id)
);

-- Tạo bảng comment_likes
CREATE TABLE comment_likes (
                               comment_id BIGINT NOT NULL,
                               user_id BIGINT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (comment_id, user_id),
                               FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE,
                               FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                               INDEX idx_user_id (user_id)
);

-- Tạo bảng comment_mentions
CREATE TABLE comment_mentions (
                                  comment_id BIGINT NOT NULL,
                                  user_id BIGINT NOT NULL,
                                  PRIMARY KEY (comment_id, user_id),
                                  FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE,
                                  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                  INDEX idx_user_id (user_id)
);

-- Tạo bảng playlists
CREATE TABLE playlists (
                           playlist_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           user_id BIGINT NOT NULL,
                           playlist_name VARCHAR(100) NOT NULL,
                           description TEXT,
                           thumbnail_url VARCHAR(255),
                           visibility ENUM('PUBLIC', 'PRIVATE', 'ONLY_ME') DEFAULT 'PUBLIC',
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                           INDEX idx_user_id (user_id),
                           INDEX idx_visibility (visibility)
);

-- Tạo bảng playlist_videos
CREATE TABLE playlist_videos (
                                 playlist_id BIGINT NOT NULL,
                                 video_id BIGINT NOT NULL,
                                 added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (playlist_id, video_id),
                                 FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id) ON DELETE CASCADE,
                                 FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                                 INDEX idx_video_id (video_id)
);

-- Tạo bảng watch_later
CREATE TABLE watch_later (
                             user_id BIGINT NOT NULL,
                             video_id BIGINT NOT NULL,
                             added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (user_id, video_id),
                             FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                             FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                             INDEX idx_video_id (video_id)
);

-- Tạo bảng video_view_history
CREATE TABLE video_view_history (
                                    user_id BIGINT NOT NULL,
                                    video_id BIGINT NOT NULL,
                                    viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    duration_watched INT NOT NULL DEFAULT 0,
                                    PRIMARY KEY (user_id, video_id, viewed_at),
                                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                    FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE
);

-- Tạo bảng report_reasons
CREATE TABLE report_reasons (
                                reason_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                reason_name VARCHAR(100) NOT NULL UNIQUE,
                                description TEXT,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng reports
CREATE TABLE reports (
                         report_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         reporter_id BIGINT NOT NULL,
                         post_id BIGINT,
                         video_id BIGINT,
                         comment_id BIGINT,
                         user_id BIGINT,
                         reason_id BIGINT NOT NULL,
                         description TEXT,
                         status ENUM('PENDING', 'APPROVED', 'REJECTED', 'RESOLVED') DEFAULT 'PENDING',
                         admin_id BIGINT,
                         admin_note TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (reporter_id) REFERENCES users(user_id) ON DELETE CASCADE,
                         FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
                         FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                         FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE,
                         FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                         FOREIGN KEY (reason_id) REFERENCES report_reasons(reason_id) ON DELETE RESTRICT,
                         FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE SET NULL,
                         CHECK (post_id IS NOT NULL OR video_id IS NOT NULL OR comment_id IS NOT NULL OR user_id IS NOT NULL)
);

-- Tạo bảng notifications
CREATE TABLE notifications (
                               notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               receiver_id BIGINT NOT NULL,
                               actor_id BIGINT,
                               type ENUM(
                                   'LIKE_POST', 'LIKE_VIDEO', 'LIKE_COMMENT',
                                   'NEW_COMMENT_POST', 'NEW_COMMENT_VIDEO',
                                   'SHARE_POST', 'SHARE_VIDEO',
                                   'NEW_FOLLOWER',
                                   'NEW_VIDEO',
                                   'REPORT_SUBMITTED', 'REPORT_RESOLVED', 'REPORT_REJECTED',
                                   'PLAYLIST_UPDATED',
                                   'SYSTEM',
                                   'MENTION'
                                   ) NOT NULL,
                               post_id BIGINT,
                               video_id BIGINT,
                               comment_id BIGINT,
                               report_id BIGINT,
                               playlist_id BIGINT,
                               content VARCHAR(500),
                               is_read BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE,
                               FOREIGN KEY (actor_id) REFERENCES users(user_id) ON DELETE SET NULL,
                               FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE SET NULL,
                               FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE SET NULL,
                               FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE SET NULL,
                               FOREIGN KEY (report_id) REFERENCES reports(report_id) ON DELETE SET NULL,
                               FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id) ON DELETE SET NULL
);

-- Tạo bảng wallets
CREATE TABLE wallets (
                         wallet_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL UNIQUE,
                         balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Tạo bảng wallet_transactions
CREATE TABLE wallet_transactions (
                                     transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     wallet_id BIGINT NOT NULL,
                                     type ENUM('DEPOSIT', 'WITHDRAW', 'PAYMENT', 'REFUND') NOT NULL,
                                     amount DECIMAL(15,2) NOT NULL,
                                     status ENUM('PENDING', 'SUCCESS', 'FAILED') DEFAULT 'PENDING',
                                     description VARCHAR(255),
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id) ON DELETE CASCADE
);

-- Tạo bảng video_copyrights
CREATE TABLE video_copyrights (
                                  copyright_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  video_id BIGINT NOT NULL,
                                  owner_id BIGINT,
                                  is_ai_generated BOOLEAN DEFAULT FALSE,
                                  status ENUM('VERIFIED', 'DISPUTED', 'VIOLATED', 'REMOVED') DEFAULT 'VERIFIED',
                                  claim_description TEXT,
                                  claimant_id BIGINT,
                                  report_id BIGINT,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                                  FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE SET NULL,
                                  FOREIGN KEY (claimant_id) REFERENCES users(user_id) ON DELETE SET NULL,
                                  FOREIGN KEY (report_id) REFERENCES reports(report_id) ON DELETE SET NULL
);

-- Tạo bảng copyright_violations
CREATE TABLE copyright_violations (
                                      violation_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      user_id BIGINT NOT NULL,
                                      video_id BIGINT NOT NULL,
                                      violation_count INT NOT NULL DEFAULT 1,
                                      penalty_applied ENUM('NONE', 'WARNING', 'TEMP_BAN', 'PERM_BAN', 'FINE') DEFAULT 'NONE',
                                      fine_amount DECIMAL(15,2) DEFAULT 0.00,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                      FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE
);

-- Tạo bảng ad_types
CREATE TABLE ad_types (
                          ad_type_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          ad_type_name VARCHAR(100) NOT NULL UNIQUE,
                          description TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng ad_campaigns
CREATE TABLE ad_campaigns (
                              campaign_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT NOT NULL,
                              wallet_id BIGINT NOT NULL,
                              campaign_name VARCHAR(255) NOT NULL,
                              objective ENUM('BRAND_AWARENESS', 'LEAD_GENERATION', 'SALES', 'VIDEO_VIEWS', 'POST_ENGAGEMENT', 'FOLLOWERS') NOT NULL,
                              budget DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                              spent_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                              start_date TIMESTAMP NOT NULL,
                              end_date TIMESTAMP,
                              status ENUM('DRAFT', 'ACTIVE', 'PAUSED', 'COMPLETED', 'CANCELLED') DEFAULT 'DRAFT',
                              target_audience TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                              FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id) ON DELETE CASCADE
);

-- Tạo bảng ads
CREATE TABLE ads (
                     ad_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                     campaign_id BIGINT NOT NULL,
                     ad_type_id BIGINT NOT NULL,
                     video_id BIGINT,
                     post_id BIGINT,
                     content TEXT,
                     status ENUM('PENDING', 'APPROVED', 'REJECTED', 'RUNNING', 'STOPPED') DEFAULT 'PENDING',
                     impressions INT NOT NULL DEFAULT 0,
                     clicks INT NOT NULL DEFAULT 0,
                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     FOREIGN KEY (campaign_id) REFERENCES ad_campaigns(campaign_id) ON DELETE CASCADE,
                     FOREIGN KEY (ad_type_id) REFERENCES ad_types(ad_type_id) ON DELETE RESTRICT,
                     FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE SET NULL,
                     FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE SET NULL
);

-- Tạo bảng video_monetization
CREATE TABLE video_monetization (
                                    monetization_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    video_id BIGINT NOT NULL UNIQUE,
                                    user_id BIGINT NOT NULL,
                                    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'DISABLED') DEFAULT 'PENDING',
                                    total_revenue DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Tạo bảng ad_revenue
CREATE TABLE ad_revenue (
                            revenue_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            monetization_id BIGINT NOT NULL,
                            ad_id BIGINT NOT NULL,
                            revenue_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                            revenue_type ENUM('CPM', 'CPC') NOT NULL,
                            impressions INT NOT NULL DEFAULT 0,
                            clicks INT NOT NULL DEFAULT 0,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (monetization_id) REFERENCES video_monetization(monetization_id) ON DELETE CASCADE,
                            FOREIGN KEY (ad_id) REFERENCES ads(ad_id) ON DELETE CASCADE
);

-- Tạo bảng video_analytics
CREATE TABLE video_analytics (
                                 analytics_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 video_id BIGINT NOT NULL,
                                 user_id BIGINT NOT NULL,
                                 period_type ENUM('DAY', 'WEEK', 'MONTH', 'YEAR') NOT NULL,
                                 period_value VARCHAR(10) NOT NULL,
                                 view_count INT NOT NULL DEFAULT 0,
                                 like_count INT NOT NULL DEFAULT 0,
                                 revenue_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 FOREIGN KEY (video_id) REFERENCES videos(video_id) ON DELETE CASCADE,
                                 FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                 UNIQUE (video_id, period_type, period_value)
);