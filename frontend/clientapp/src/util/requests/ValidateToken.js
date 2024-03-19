export const ValidateToken = async () => {
    const token = localStorage.getItem("jwt");
    if (!token) return false;
    try {
        const response = await fetch(`http://localhost:8080/auth/valid`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        if (!response.ok) {
            throw new Error(`Error validating token: ${response.statusText}`);
        }
        return true;
    } catch (error) {
        return false;
    }
};