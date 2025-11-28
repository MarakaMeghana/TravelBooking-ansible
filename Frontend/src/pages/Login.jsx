import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../config";
import { useAuth } from "../context/AuthContext"; // ✅ Added for context
import "./Login.css";

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth(); // ✅ Access AuthContext login method

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    console.log("Login clicked ✅");

    try {
      // ✅ Send email & password to backend
      const res = await loginUser({ email, password });
      console.log("Response from backend:", res);

      if (res.status === 200) {
        // ✅ Save user data in both context and localStorage
        login(res.data);
        localStorage.setItem("userId", res.data.id);
        localStorage.setItem("userEmail", res.data.email);
        localStorage.setItem("userName", res.data.name);

        setMessage("Login successful ✅");
        navigate("/dashboard");
      } else {
        setMessage("Invalid login ❌");
      }
    } catch (err) {
      console.error("Login error:", err);
      if (err.response && err.response.status === 401) {
        setMessage("Invalid email or password ❌");
      } else {
        setMessage("Error: " + (err.response?.data || err.message));
      }
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2 className="login-title">Customer Login</h2>
        <form onSubmit={handleLogin} className="login-form">
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit">Login</button>
        </form>
        {message && <p>{message}</p>}
        <p style={{ marginTop: "15px" }}>
          Don’t have an account?{" "}
          <span
            style={{ color: "#1e90ff", cursor: "pointer" }}
            onClick={() => navigate("/register")}
          >
            Sign Up
          </span>
        </p>
      </div>
    </div>
  );
};

export default Login;
