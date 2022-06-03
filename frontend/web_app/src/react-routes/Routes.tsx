import { Routes, Route, Navigate } from 'react-router-dom';
import Login from '../modules/login/Index';
import LayoutApp from '../layouts/Layout';
import Home from '../modules/home/Index';
import AccountDetail from '../modules/account/detail/Index';
import PrivateRoute from './PrivateRoute';
import ChangePassword from '../modules/account/change-password/Index';
import Follow from '../modules/follow/index';
import Work from '../modules/work';
import Register from '../modules/register/Index';
import Market from '../modules/market';
import ForgetPass from '../modules/forget-password';

const IndexRoutes = () => {
    return (
        <>
            <Routes>
                <Route path='/login' element={<Login />} />
                <Route path='/register' element={<Register />} />
                <Route path='/forget-pass' element={<ForgetPass />} />
                <Route path="/" element={<PrivateRoute><LayoutApp /></PrivateRoute>}>
                    <Route path='' element={<PrivateRoute><Home /></PrivateRoute>} />

                    {/* account */}
                    <Route path='account/detail' element={<PrivateRoute><AccountDetail /></PrivateRoute>} />
                    <Route path='account/change-password' element={<PrivateRoute><ChangePassword /></PrivateRoute>} />

                    {/* follow */}
                    <Route path='follow' element={<PrivateRoute><Follow/></PrivateRoute>} />
                    <Route path='work' element={<PrivateRoute><Work/></PrivateRoute>} />
                    <Route path='market' element={<PrivateRoute><Market/></PrivateRoute>} />


                    <Route path='*' element={<Navigate to="/" />} />
                </Route>
            </Routes>
        </>
    );
}

export default IndexRoutes;
