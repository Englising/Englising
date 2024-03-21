import '../src/index.css'
import './App.css'
import {
  createBrowserRouter,
  RouterProvider
} from 'react-router-dom'
import SelectMultiPage from './pages/SelectMultiPage'
import IndexPage from './pages/IndexPage'
import SelectSingle1Page from './pages/SelectSingle1Page.tsx'
import SelectSingle2Page from './pages/SelectSingle2Page.tsx'
import SelectSingle3Page from './pages/SelectSingle3Page.tsx'
import SettingMultiPage from './pages/SettingMultiPage'
import WaitingRoomPage from './pages/WaitingRoomPage'
import SinglePlay from './pages/SinglePage'
import Multiplay from './pages/MultiplayPage'
import WordListPage from './pages/WordListPage'
import SidebarLayout from './pages/SidebarLayout'


function App() {

  const router = createBrowserRouter([
    {
      path: "/",
      element: <IndexPage/>,
    },    
    {
      path: "/englising",
      element: <SidebarLayout />,
      children: [
        {
          path:"selectSingle1",
          element: <SelectSingle1Page />,
        },
        {
          path:"selectSingle2",
          element: <SelectSingle2Page />,
        },
        {
          path:"selectSingle3",
          element: <SelectSingle3Page />,
        },
        {
          path:"selectMulti",
          element: <SelectMultiPage /> 
        },
        {
          path:"settingMulti",
          element: <SettingMultiPage /> 
        },
        {
          path:"wordList",
          element: <WordListPage /> 
        }
      ]
    },
    {
      path: "/singlePlay",
      element: <SinglePlay/>
    },
    {
      path: "/waitroom/:multiId",
      element: <WaitingRoomPage/>
    },
    {
      path: "/multiPlay/:multiId",
      element: <Multiplay/>
    },
  ])

  return (
    <div>
      <div>
      <RouterProvider router={router} fallbackElement={<p>Loading...</p>} />
      </div>
    </div>
  )
}

export default App