import { useEffect } from "react";
import { useLiveStreamMutation } from "./redux/LiveApi";

const Live = () => {
    const [stream] = useLiveStreamMutation();
   
    const subscribe = async () => {
          const events = new EventSource(process.env.REACT_APP_API_URL + "/sse/time");
          events.onmessage = event => {
            console.log(event)
         
          };
      };
    useEffect(() => {
        subscribe();
    }, []);
    return (
        <div>
            hello
        </div>
    )
}
export default Live;