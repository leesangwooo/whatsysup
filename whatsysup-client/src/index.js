import React from 'react';
import ReactDOM from 'react-dom';
import 'react-quill/dist/quill.snow.css'; // ES6
import './index.css';
import App from './app/App';
import registerServiceWorker from './registerServiceWorker';
import { BrowserRouter as Router } from 'react-router-dom';

ReactDOM.render(
    <Router>
        <App />
    </Router>,
    document.getElementById('root')
);

registerServiceWorker();
