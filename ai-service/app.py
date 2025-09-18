from flask import Flask, jsonify

app = Flask(__name__)

@app.route('/api/ai/generate', methods=['POST'])
def generate():
    """
    This endpoint will eventually contain your AI logic.
    For now, it returns a dummy response.
    """
    return jsonify({"generated_text": "This is a response from the AI service."})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
