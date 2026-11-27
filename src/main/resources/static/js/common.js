function getRedirectURL(url) {
    const contextPath = "/community";
    const current = location.pathname;
    return current.substring(contextPath.length);
}