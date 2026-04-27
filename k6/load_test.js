import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 10 },
        { duration: '1m',  target: 50 },
        { duration: '30s', target: 0  },
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],
        http_req_failed:   ['rate<0.01'],
    },
};

const BASE_URL = 'http://localhost:8080';

export default function () {
    const booksRes = http.get(`${BASE_URL}/api/books`);
    check(booksRes, {
        'books status 200': (r) => r.status === 200,
        'books response time < 500ms': (r) => r.timings.duration < 500,
    });

    const usersRes = http.get(`${BASE_URL}/api/users`);
    check(usersRes, {
        'users status 200': (r) => r.status === 200,
    });

    const notFoundRes = http.get(`${BASE_URL}/api/books/99999`);
    check(notFoundRes, {
        '404 döner': (r) => r.status === 404,
    });

    sleep(1);
}
