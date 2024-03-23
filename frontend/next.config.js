/** @type {import('next').NextConfig} */

module.exports = () => {
    const rewrites = () => {
        return [{ source: '/api/:path*', destination: 'http://localhost:8080/api/:path*' }];
    };
    return {
        rewrites
    };
};

// const nextConfig = {
//     reactStrictMode: true,
//     async redirects() {
//         return [
//             {
//                 source: '/apps/mail',
//                 destination: '/apps/mail/inbox',
//                 permanent: true
//             }
//         ];
//     }
// };
