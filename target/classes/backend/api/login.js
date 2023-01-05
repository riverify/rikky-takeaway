function loginApi(data) {
    return $axios({
        'url': '/employee/login',
        'method': 'post',
        data
    })
}

function logoutApi() {
    return $axios({
        'url': '/employee/logout',
        'method': 'post',
    })
}
