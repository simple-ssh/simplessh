import React from 'react'
import ReactDOM from 'react-dom'


class ProductList extends React.Component{

  constructor(props) {
       super(props);
       this.state = {rows : [] }
 }

  componentDidMount(){


   }

  render(){
    return(
    <div>
    products-list -front
       {
       this.state.rows.map(

                row =>
                    <div>
                       {row.productid}
                    </div>
                )
       }
       </div>
    )
  }
}

export default ProductList;