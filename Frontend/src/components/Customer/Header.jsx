import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext"; // ✅ use custom hook
import { Link, useNavigate } from "react-router-dom";

const Header = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const [displayName, setDisplayName] = useState(
    localStorage.getItem("userName") || user?.name || "Guest"
  );

  // ✅ Sync name changes from localStorage or context
  useEffect(() => {
    const handleStorageChange = () => {
      setDisplayName(localStorage.getItem("userName") || user?.name || "Guest");
    };

    window.addEventListener("storage", handleStorageChange);

    if (user?.name) {
      setDisplayName(user.name);
    }

    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, [user]);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <header
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "15px 40px",
        backgroundColor: "#008080",
        color: "#fff",
        boxShadow: "0 2px 6px rgba(0, 0, 0, 0.1)",
        position: "sticky",
        top: 0,
        zIndex: 1000,
      }}
    >
      <h2 style={{ margin: 0, fontWeight: "bold", color: "#e1ecebff" }}>
        TravelEase
      </h2>

      <nav>
        <ul
          style={{
            display: "flex",
            listStyle: "none",
            gap: "20px",
            margin: 0,
            padding: 0,
          }}
        >
          <li>
            <Link
              to="/dashboard"
              style={{
                color: "#f5efefff",
                textDecoration: "none",
                fontWeight: "500",
              }}
            >
              Home
            </Link>
          </li>
          <li>
            <Link
              to="/booking"
              style={{
                color: "#eee9e9ff",
                textDecoration: "none",
                fontWeight: "500",
              }}
            >
              Bookings
            </Link>
          </li>
          <li>
            <Link
              to="/profile"
              style={{
                color: "#f4f0f0ff",
                textDecoration: "none",
                fontWeight: "500",
              }}
            >
              Profile
            </Link>
          </li>
        </ul>
      </nav>

      <div>
        <span style={{ marginRight: "20px", color: "#fff", fontWeight: "500" }}>
          Hi, {displayName} 👋
        </span>
        {user && (
          <button
            onClick={handleLogout}
            style={{
              backgroundColor: "#044e6bff",
              color: "#fff",
              border: "none",
              padding: "6px 12px",
              borderRadius: "6px",
              cursor: "pointer",
              fontWeight: "500",
              transition: "0.3s",
            }}
            onMouseOver={(e) => (e.target.style.backgroundColor = "#00796b")}
            onMouseOut={(e) => (e.target.style.backgroundColor = "#044e6bff")}
          >
            Logout
          </button>
        )}
      </div>
    </header>
  );
};

export default Header;
