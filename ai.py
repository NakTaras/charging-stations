import numpy as np
from flask import Flask, request, jsonify
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import StandardScaler

app = Flask(__name__)


@app.route('/calculate-ratings', methods=['POST'])
def calculate_ratings():
    data = request.get_json()

    # Визначаємо ваги для критеріїв
    weights = {
        "price": -0.2,  # Мінімізація
        "capacityWh": 0.3,
        "outputPowerW": 0.2,
        "chargingTimeHours": -0.1,  # Мінімізація
        "weightKg": -0.1,  # Мінімізація
        "peakPowerW": 0.1,
        "solarPanelSupport": 0.05,
        "wirelessCharging": 0.05
    }

    # Нормалізація даних
    def normalize(value, min_val, max_val):
        return (value - min_val) / (max_val - min_val) if max_val != min_val else 0

    # Знаходимо межі для нормалізації
    prices = [station['price'] for station in data]
    capacities = [station['parameters']['capacityWh'] for station in data]
    output_powers = [station['parameters']['outputPowerW'] for station in data]
    charging_times = [station['parameters']['chargingTimeHours'] for station in data]
    weights_kg = [station['parameters']['weightKg'] for station in data]
    peak_powers = [station['parameters']['peakPowerW'] for station in data]

    min_price, max_price = min(prices), max(prices)
    min_capacity, max_capacity = min(capacities), max(capacities)
    min_output_power, max_output_power = min(output_powers), max(output_powers)
    min_charging_time, max_charging_time = min(charging_times), max(charging_times)
    min_weight, max_weight = min(weights_kg), max(weights_kg)
    min_peak_power, max_peak_power = min(peak_powers), max(peak_powers)

    # Розрахунок рейтингу для кожної станції
    result = []
    for station in data:
        rating = 0

        # Нормалізовані значення
        rating += weights["price"] * normalize(station['price'], min_price, max_price)
        rating += weights["capacityWh"] * normalize(station['parameters']['capacityWh'], min_capacity, max_capacity)
        rating += weights["outputPowerW"] * normalize(station['parameters']['outputPowerW'], min_output_power,
                                                      max_output_power)
        rating += weights["chargingTimeHours"] * normalize(station['parameters']['chargingTimeHours'],
                                                           min_charging_time, max_charging_time)
        rating += weights["weightKg"] * normalize(station['parameters']['weightKg'], min_weight, max_weight)
        rating += weights["peakPowerW"] * normalize(station['parameters']['peakPowerW'], min_peak_power, max_peak_power)

        # Логічні значення
        rating += weights["solarPanelSupport"] * (1 if station['features']['solarPanelSupport'] else 0)
        rating += weights["wirelessCharging"] * (1 if station['features']['wirelessCharging'] else 0)

        # Масштабування рейтингу до 0.0–10.0
        scaled_rating = (rating - -0.5) / (0.5 - -0.5) * 10  # Масштабуємо значення до діапазону 0-10
        scaled_rating = max(0.0, min(10.0, scaled_rating))  # Обмеження у межах 0-10

        # Формуємо результат у потрібному форматі
        result.append({
            "chargingStation": station,
            "rating": round(scaled_rating, 1)
        })

    return jsonify(result)


# Ваги параметрів
WEIGHTS = {
    "price": -0.3,  # Менша ціна краща
    "capacityWh": 0.4,
    "outputPowerW": 0.3,
    "chargingTimeHours": -0.2,  # Менший час заряджання кращий
    "weightKg": -0.1,  # Менша вага краща
    "peakPowerW": 0.2,
    "solarPanelSupport": 0.15,
    "wirelessCharging": 0.05
}


def normalize(value, min_val, max_val):
    """
    Нормалізує значення до діапазону [0, 1].
    """
    if min_val == max_val:
        return 1.0 if value == max_val else 0.0
    return (value - min_val) / (max_val - min_val)


def calculate_score(station, input_params, global_min_max):
    """
    Обчислює зважений сумарний бал для зарядної станції.
    """
    score = 0
    for key, weight in WEIGHTS.items():
        # Отримуємо значення параметра
        if key in station["parameters"]:
            value = station["parameters"][key]
        elif key in station["features"]:
            value = int(station["features"][key])  # Перетворення bool на int
        elif key in station:
            value = station[key]
        else:
            continue

        # Використовуємо глобальні мін/макс значення для нормалізації
        min_val, max_val = global_min_max[key]

        # Нормалізація
        normalized_value = normalize(value, min_val, max_val)

        # Розрахунок зваженого балу
        score += weight * normalized_value
    return score


@app.route('/optimize-stations', methods=['POST'])
def optimize_stations():
    """
    Ендпоінт для оптимізації вибору зарядних станцій.
    """
    data = request.json
    stations = data["chargingStations"]
    input_params = data["inputParameters"]

    # Знаходимо глобальні мінімальні та максимальні значення для кожного параметра
    global_min_max = {}
    for key in WEIGHTS.keys():
        values = []
        for station in stations:
            if key in station["parameters"]:
                values.append(station["parameters"][key])
            elif key in station["features"]:
                values.append(int(station["features"][key]))  # Перетворення bool на int
            elif key in station:
                values.append(station[key])

        # Додаємо вхідне значення до діапазону
        if key in input_params:
            values.append(input_params[key])

        # Обчислення мін/макс
        global_min_max[key] = (min(values), max(values))

    # Розрахунок рейтингу для кожної станції
    for station in stations:
        station["rating"] = calculate_score(station, input_params, global_min_max)

    # Сортуємо за рейтингом у порядку спадання
    sorted_stations = sorted(stations, key=lambda x: x["rating"], reverse=True)

    # Повертаємо топ-3 станції
    return jsonify(sorted_stations[:3])


# Тренувальні дані з 40 станцій
X_train = np.array([
    [1002, 1000, 5.5, 10, 2000, 1, 0],  # Jackery Explorer 1000 (Camping)
    [1516, 2000, 2.5, 20.7, 3500, 1, 0],  # Goal Zero Yeti 1500X (Home Use)
    [2000, 2000, 3.5, 27.5, 4800, 1, 1],  # Bluetti AC200P (Work Use)
    [1260, 1800, 1.6, 14, 3300, 1, 0],  # EcoFlow Delta 1300 (Home Use)
    [434, 300, 7, 4.2, 400, 0, 0],  # Anker Powerhouse 400 (Camping)

    # Додаємо ще станцій (35 додаткових станцій)
    [800, 600, 6.0, 8, 1500, 1, 0],  # Camping
    [2500, 2200, 4.0, 22, 4000, 1, 1],  # Work Use
    [1500, 1200, 2.8, 18, 3000, 1, 0],  # Home Use
    [1000, 1000, 5.0, 10, 2000, 0, 0],  # Camping
    [2000, 1500, 3.0, 25, 3500, 1, 1],  # Work Use
    [1200, 900, 6.5, 12, 2500, 0, 1],  # Camping
    [500, 450, 7.0, 6, 1000, 0, 0],  # Camping
    [1800, 2000, 3.5, 20, 3500, 1, 1],  # Work Use
    [1600, 1400, 4.0, 15, 3300, 1, 0],  # Home Use
    [1400, 1000, 5.0, 12, 2200, 1, 0],  # Home Use
    [1300, 1100, 5.2, 14, 2500, 1, 1],  # Work Use
    [1200, 1000, 5.5, 11, 2300, 0, 0],  # Camping
    [800, 700, 6.0, 10, 1800, 1, 0],  # Camping
    [1800, 1600, 3.5, 18, 4000, 1, 1],  # Work Use
    [2200, 1800, 2.8, 19, 3000, 1, 0],  # Home Use
    [700, 500, 6.5, 8, 1500, 1, 0],  # Camping
    [2500, 2200, 3.5, 21, 4000, 1, 1],  # Work Use
    [1300, 1200, 4.0, 16, 3000, 1, 0],  # Home Use
    [1100, 900, 5.2, 13, 2000, 1, 0],  # Home Use
    [800, 600, 7.0, 10, 1800, 0, 0],  # Camping
    [2200, 2000, 3.0, 25, 3800, 1, 1],  # Work Use
    [1500, 1300, 4.5, 19, 3500, 1, 0],  # Home Use
    [1100, 950, 6.0, 14, 2400, 0, 0],  # Camping
    [1600, 1400, 5.0, 18, 3200, 1, 0],  # Home Use
    [2000, 1800, 3.0, 22, 4000, 1, 1],  # Work Use
    [1000, 850, 6.5, 12, 2000, 0, 0],  # Camping
    [1200, 1100, 5.2, 13, 2700, 1, 0],  # Home Use
    [2300, 2000, 3.0, 20, 4000, 1, 1],  # Work Use
    [1300, 1200, 5.5, 14, 2800, 0, 1],  # Camping
    [2500, 2200, 3.2, 24, 4500, 1, 1],  # Work Use
    [1500, 1400, 4.5, 17, 3000, 1, 0],  # Home Use
    [900, 800, 7.0, 9, 1500, 0, 0],  # Camping
    [1600, 1500, 3.8, 19, 3800, 1, 0],  # Home Use
    [1000, 900, 6.5, 8, 1700, 0, 0],  # Camping
    [1800, 1600, 3.5, 20, 3600, 1, 0],  # Home Use
])

# Мітки класів (0: Home Use, 1: Work Use, 2: Camping)
y_train = np.array([
    2, 0, 1, 0, 2, 2, 1, 0, 2, 1, 2, 2, 1, 0, 0, 1, 0, 1, 0, 2, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 2, 1, 0, 1, 1, 0, 0,
    1
])

# Масштабування даних
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)

# Навчання моделі
model = LogisticRegression(max_iter=200)
model.fit(X_train_scaled, y_train)

# Маппінг результату класифікації на типи
class_mapping = {0: "Home Use", 1: "Work Use", 2: "Camping"}


# Створення ендпоінту для класифікації
@app.route('/classify', methods=['POST'])
def classify_station():
    data = request.get_json()

    # Перевірка, чи всі необхідні параметри є в запиті
    required_fields = ["capacityWh", "outputPowerW", "chargingTimeHours", "weightKg", "peakPowerW", "solarPanelSupport",
                       "wirelessCharging"]
    for field in required_fields:
        if field not in data:
            return jsonify({"error": f"Missing {field} in input data"}), 400

    # Підготовка даних для передбачення
    input_data = np.array([
        data["capacityWh"],
        data["outputPowerW"],
        data["chargingTimeHours"],
        data["weightKg"],
        data["peakPowerW"],
        int(data["solarPanelSupport"]),
        int(data["wirelessCharging"])
    ]).reshape(1, -1)

    # Масштабування вхідних даних
    input_data_scaled = scaler.transform(input_data)

    # Класифікація
    prediction = model.predict(input_data_scaled)
    predicted_class = class_mapping[prediction[0]]

    return jsonify({"predicted_class": predicted_class})


if __name__ == '__main__':
    app.run(debug=True, port=5001)
