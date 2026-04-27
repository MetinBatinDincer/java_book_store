import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 5   },
        { duration: '10s', target: 200 },
        { duration: '10s', target: 5   },
    ],
    thresholds: {
        http_req_duration: ['p(99)<2000'],
        http_req_failed:   ['rate<0.05'],
    },
};

const BASE_URL = 'http://localhost:8080';

export default function () {
    const res = http.get(`${BASE_URL}/api/books`);
    check(res, {
        'status 200': (r) => r.status === 200,
    });
    sleep(0.5);
}
