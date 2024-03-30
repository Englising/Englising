import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getSinglePlayResultData } from "../util/SinglePlayAPI";

const SingleResultPlage = () => {
    // 1. 싱글플레이 아이디 넘어와야 함 (이건 쿼리스트링)
    // 2. 똑같이 배경이랑 타이틀 이름 넘어와야 함 (location의 데이터로)
    // 3. 뒤로가기 막아보기 - 후작업

    const { singlePlayId } = useParams<{
        singlePlayId: string;
    }>();

    //const { resultData, setResultData } = useState<number>();
    useEffect(() => {
        const data = {
            "singlePlayId": parseInt(singlePlayId || "0"),
        }
      
        const getData = async () => {
            try {
                const singleResultData = await getSinglePlayResultData(data);
                console.log("야미",singleResultData); 

            } catch (error) {
                console.error('Error fetching data:', error)
            }
        }
        getData();
    }, [])
    
    return (
        <div>
            오잉
        </div>
    )

}
export default SingleResultPlage;