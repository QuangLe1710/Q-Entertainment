import { Button } from "@base-ui/react/button";
import { Link } from "react-router-dom";


export default function HomePage() {
    return (
        <div className="flex flex-col items-center justify-center gap-4">
            <h1>HOME PAGE</h1>
        
            <Button>
                <Link to={'/authpage'}>LOG IN</Link>
            </Button>
        </div>
    );
}