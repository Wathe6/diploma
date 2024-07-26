import React from 'react';

const PageIcon = (props) => (
    <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        strokeWidth={1.5}
        stroke="currentColor"
        {...props}
    >
        <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M3 3h18v18H3V3z"
        />
        <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M9 3v4.5a.75.75 0 0 0 .75.75h4.5V3"
        />
    </svg>
);

export default PageIcon;
