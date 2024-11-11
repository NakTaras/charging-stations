-- Створення бази даних, якщо не існує
CREATE DATABASE IF NOT EXISTS charging_stations_db;
USE charging_stations_db;

-- Видалення таблиць, якщо існують
DROP TABLE IF EXISTS charging_stations;
DROP TABLE IF EXISTS features;
DROP TABLE IF EXISTS parameters;
DROP TABLE IF EXISTS manufacturers;
DROP TABLE IF EXISTS countries;
DROP TABLE IF EXISTS station_classes;

-- Таблиця: countries
CREATE TABLE IF NOT EXISTS countries (
                                         id INT PRIMARY KEY AUTO_INCREMENT,
                                         country_name VARCHAR(100) NOT NULL UNIQUE
);

-- Таблиця: manufacturers
CREATE TABLE IF NOT EXISTS manufacturers (
                                             id INT PRIMARY KEY AUTO_INCREMENT,
                                             manufacturer_name VARCHAR(100) NOT NULL UNIQUE,
                                             country_id INT,
                                             FOREIGN KEY (country_id) REFERENCES countries(id)
);

-- Таблиця: parameters
CREATE TABLE IF NOT EXISTS parameters (
                                          id INT PRIMARY KEY AUTO_INCREMENT,
                                          capacity_wh INT NOT NULL,
                                          output_power_w INT NOT NULL,
                                          charging_time_hours DECIMAL(5,2) NOT NULL,
                                          weight_kg DECIMAL(5,2) NOT NULL,
                                          peak_power_w INT NOT NULL
);

-- Таблиця: features
CREATE TABLE IF NOT EXISTS features (
                                        id INT PRIMARY KEY AUTO_INCREMENT,
                                        solar_panel_support BOOLEAN NOT NULL,
                                        wireless_charging BOOLEAN NOT NULL
);

-- Таблиця: station_classes
CREATE TABLE IF NOT EXISTS station_classes (
                                               id INT PRIMARY KEY AUTO_INCREMENT,
                                               class_name VARCHAR(50) NOT NULL UNIQUE
);

-- Таблиця: charging_stations
CREATE TABLE IF NOT EXISTS charging_stations (
                                                 id INT PRIMARY KEY AUTO_INCREMENT,
                                                 model_name VARCHAR(100) NOT NULL UNIQUE,
                                                 manufacturer_id INT,
                                                 parameters_id INT,
                                                 features_id INT,
                                                 station_class_id INT,
                                                 description TEXT NOT NULL,
                                                 price DECIMAL(10,2) NOT NULL,
                                                 purchase_link VARCHAR(255) NOT NULL,
                                                 image_link VARCHAR(255) NOT NULL,
                                                 FOREIGN KEY (manufacturer_id) REFERENCES manufacturers(id),
                                                 FOREIGN KEY (parameters_id) REFERENCES parameters(id),
                                                 FOREIGN KEY (features_id) REFERENCES features(id),
                                                 FOREIGN KEY (station_class_id) REFERENCES station_classes(id)
);

-- Вставка даних в таблицю countries
INSERT IGNORE INTO countries (country_name) VALUES
                                                ('USA'),
                                                ('China'),
                                                ('Germany');

-- Вставка даних в таблицю manufacturers
INSERT IGNORE INTO manufacturers (manufacturer_name, country_id) VALUES
                                                                     ('EcoFlow', 1),
                                                                     ('Jackery', 2),
                                                                     ('Bluetti', 2),
                                                                     ('Anker', 3),
                                                                     ('Goal Zero', 1);

-- Вставка даних в таблицю parameters
INSERT IGNORE INTO parameters (capacity_wh, output_power_w, charging_time_hours, weight_kg, peak_power_w) VALUES
                                                                                                              (1260, 1800, 1.6, 14.0, 3300),    -- EcoFlow Delta 1300
                                                                                                              (1002, 1000, 5.5, 10.0, 2000),    -- Jackery Explorer 1000
                                                                                                              (2000, 2000, 3.5, 27.5, 4800),    -- Bluetti AC200P
                                                                                                              (434, 300, 7.0, 4.2, 400),        -- Anker Powerhouse 400
                                                                                                              (1516, 2000, 2.5, 20.7, 3500);    -- Goal Zero Yeti 1500X

-- Вставка даних в таблицю features
INSERT IGNORE INTO features (solar_panel_support, wireless_charging) VALUES
                                                                         (TRUE, FALSE),    -- EcoFlow Delta 1300
                                                                         (TRUE, FALSE),    -- Jackery Explorer 1000
                                                                         (TRUE, TRUE),     -- Bluetti AC200P
                                                                         (FALSE, FALSE),   -- Anker Powerhouse 400
                                                                         (TRUE, FALSE);    -- Goal Zero Yeti 1500X

-- Вставка даних в таблицю station_classes
INSERT IGNORE INTO station_classes (class_name) VALUES
                                                    ('Home Use'),
                                                    ('Work Use'),
                                                    ('Camping');

-- Вставка даних в таблицю charging_stations
INSERT IGNORE INTO charging_stations (model_name, manufacturer_id, parameters_id, features_id, station_class_id, description, price, purchase_link, image_link) VALUES
                                                                                                                                                                    ('EcoFlow Delta 1300', 1, 1, 1, 1, 'Powerful station with solar panel support.', 1399.99, 'https://hotline.ua/ua/mobile-zaryadnye-stancii/ecoflow-delta/?tab=prices&gad_source=1&gclid=Cj0KCQiA88a5BhDPARIsAFj595ha2E6ea_nzamqKvYQDTotQEs6P3W7CqZR_f6nMHqO1Q9I6W0h9GRgaAiMMEALw_wcB#gallery', 'https://hotline.ua/img/tx/373/373160099_s265.jpg'),
                                                                                                                                                                    ('Jackery Explorer 1000', 2, 2, 2, 3, 'Portable power station for camping and outdoor use.', 999.99, 'https://hotline.ua/ua/mobile-zaryadnye-stancii/jackery-explorer-1000eu/', 'https://hotline.ua/img/tx/462/462309242_s265.jpg'),
                                                                                                                                                                    ('Bluetti AC200P', 3, 3, 3, 2, 'High-capacity station with multiple output options and solar charging.', 1599.99, 'https://hotline.ua/ua/mobile-zaryadnye-stancii/bluetti-ac200p/', 'https://hotline.ua/img/tx/359/359854558_s265.jpg'),
                                                                                                                                                                    ('Anker Powerhouse 400', 4, 4, 4, 3, 'Compact and lightweight power station for small devices.', 399.99, 'https://hotline.ua/ua/mobile-zaryadnye-stancii/anker-522-powerhouse-a1721311/', 'https://hotline.ua/img/tx/491/491302325_s265.jpg'),
                                                                                                                                                                    ('Goal Zero Yeti 1500X', 5, 5, 5, 1, 'Advanced portable power with high peak power and solar charging.', 1999.99, 'https://hotline.ua/ua/mobile-zaryadnye-stancii/goal-zero-yeti-1500x/', 'https://hotline.ua/img/tx/354/354930395_s265.jpg');
